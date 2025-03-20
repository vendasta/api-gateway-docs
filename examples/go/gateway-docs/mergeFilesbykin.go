package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"github.com/getkin/kin-openapi/openapi3"
	"log"
	"os"
	"path/filepath"

	"github.com/getkin/kin-openapi/openapi2"
)

const outputFile1 = "../../../openapi/grpc/grpc_api.swagger1.json"

func main() {
	// Define input directory as a command-line flag
	inputDir := flag.String("input", "", "Directory containing Swagger JSON files")
	flag.Parse()

	// Validate required arguments
	if *inputDir == "" {
		log.Fatal("Usage: go run mergeOpenAPISpecs.go --input <input_dir>")
	}

	fmt.Println("Starting Swagger JSON Merge Process...")

	// Collect Swagger JSON files
	swaggerFiles, err := getSwaggerFiles(*inputDir)
	if err != nil {
		log.Fatalf("Failed to collect Swagger files: %v", err)
	}

	fmt.Printf("🔍 Found %d Swagger files\n", len(swaggerFiles))

	// Merge the files using openapi2.T
	mergedSwagger, err := mergeSwaggerFiles1(swaggerFiles)
	if err != nil {
		log.Fatalf("Failed to merge Swagger files: %v", err)
	}

	// Write the merged file
	if err := writeSwaggerToFile(outputFile1, mergedSwagger); err != nil {
		log.Fatalf("Error writing merged Swagger file: %v", err)
	}

	fmt.Println("🎉 Successfully merged Swagger files into:", outputFile1)
}

// **🔹 Collects all `api.swagger.json` files in the given directory**
func getSwaggerFiles(dir string) ([]string, error) {
	var files []string
	err := filepath.Walk(dir, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && info.Name() == "api.swagger.json" {
			files = append(files, path)
		}
		return nil
	})
	return files, err
}

// **🔹 Merges multiple Swagger 2.0 files using openapi2.T struct**
func mergeSwaggerFiles1(files []string) (*openapi2.T, error) {
	// Initialize a new OpenAPI 2.0 spec
	mergedSpec := &openapi2.T{
		Swagger: "2.0",
		Info: openapi3.Info{
			Title:       "gRPC Endpoints",
			Description: "gRPC Endpoints for Vendasta APIs",
			Version:     "1.0.0",
		},
		//Info: &openapi2.Info{
		//	Title:       "gRPC Endpoints",
		//	Description: "Not implemented",
		//	Version:     "1.0.0",
		//},
		Paths:       make(map[string]*openapi2.PathItem),
		Definitions: make(map[string]*openapi2.SchemaRef),
	}

	// Loop through files and merge
	for _, file := range files {
		fmt.Println("🔹 Processing:", file)

		// Load and parse Swagger JSON
		swagger, err := loadSwaggerFile(file)
		if err != nil {
			log.Printf("⚠️ Skipping %s (failed to load): %v\n", file, err)
			continue
		}

		// Merge paths
		for k, v := range swagger.Paths {
			mergedSpec.Paths[k] = v
		}

		// Merge definitions
		for k, v := range swagger.Definitions {
			mergedSpec.Definitions[k] = v
		}
	}

	return mergedSpec, nil
}

// **🔹 Loads a single Swagger 2.0 file into openapi2.T**
func loadSwaggerFile(filePath string) (*openapi2.T, error) {
	data, err := os.ReadFile(filePath)
	if err != nil {
		return nil, err
	}

	var spec openapi2.T
	if err := json.Unmarshal(data, &spec); err != nil {
		return nil, err
	}

	return &spec, nil
}

// **🔹 Writes merged spec to a JSON file**
func writeSwaggerToFile(outputPath string, spec *openapi2.T) error {
	outputDir := filepath.Dir(outputPath)
	if err := os.MkdirAll(outputDir, os.ModePerm); err != nil {
		return fmt.Errorf("failed to create output directory: %v", err)
	}

	jsonData, err := json.MarshalIndent(spec, "", "  ")
	if err != nil {
		return fmt.Errorf("failed to marshal JSON: %v", err)
	}

	return os.WriteFile(outputPath, jsonData, 0644)
}
