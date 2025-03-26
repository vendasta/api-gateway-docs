package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"path/filepath"
)

//const inputDir1 = "./vendastaapisgen1" // Directory containing Swagger JSON files

// const outputFile = "./openapi/grpc/grpc_api.swagger.json"
const outputFile = "../../../openapi/grpc/grpc_api.swagger.json"

func main() {

	// input directory will be dynamic from the build process , the branch of generated files will be passed as input
	// Example: go run main.go --input ./vendastaapisgen1
	inputDir := flag.String("input", "", "Directory containing Swagger JSON files")
	flag.Parse()
	// Validate required arguments
	if *inputDir == "" {
		log.Fatal("Usage: go run mergeOpenAPISpecs.go --input <input_dir> ")
	}
	fmt.Println("Starting Swagger JSON Merge Process...")

	// Collect all Swagger JSON files
	swaggerFiles, err := getSwaggerFiles1(*inputDir)
	if err != nil {
		log.Fatalf("Failed to collect Swagger files: %v", err)
	}

	fmt.Printf("Found %d Swagger files\n", len(swaggerFiles))

	// Merge the files
	mergedSwagger, err := mergeSwaggerFiles(swaggerFiles)
	if err != nil {
		log.Fatalf("Failed to merge Swagger files: %v", err)
	}

	// Write the merged file
	if err := ioutil.WriteFile(outputFile, mergedSwagger, 0644); err != nil {
		log.Fatalf("Error writing merged Swagger file: %v", err)
	}

	fmt.Println("Successfully merged Swagger files into:", outputFile)
}

// getSwaggerFiles scans the input directory for all `api.swagger.json` files
func getSwaggerFiles1(dir string) ([]string, error) {
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

// mergeSwaggerFiles reads and merges multiple JSON Swagger files
func mergeSwaggerFiles(files []string) ([]byte, error) {
	mergedData := make(map[string]interface{})
	mergedData["swagger"] = "2.0"

	for _, file := range files {
		fmt.Println("Processing:", file)

		//data, err := ioutil.ReadFile(file)
		data, err := os.ReadFile(file)
		if err != nil {
			log.Printf("Skipping %s (failed to read): %v\n", file, err)
			continue
		}

		var swagger map[string]interface{}
		if err := json.Unmarshal(data, &swagger); err != nil {
			log.Printf("Skipping %s (invalid JSON): %v\n", file, err)
			continue
		}

		// Merge top-level fields
		mergeField("paths", swagger, mergedData)
		mergeField("definitions", swagger, mergedData)
		mergeField("components", swagger, mergedData)
		mergeField("info", swagger, mergedData) // API Metadata

		// Ensure 'info' exists with a valid title
		if _, exists := mergedData["info"]; !exists {
			mergedData["info"] = map[string]interface{}{
				"title":   "Merged API",
				"version": "1.0.0",
			}
		}
	}

	// Convert merged data to JSON
	mergedJSON, err := json.MarshalIndent(mergedData, "", "  ")
	if err != nil {
		return nil, err
	}

	return mergedJSON, nil
}

// mergeField merges specific fields like "paths" or "definitions"
func mergeField(field string, source, target map[string]interface{}) {
	if sourceField, ok := source[field].(map[string]interface{}); ok {
		if _, exists := target[field]; !exists {
			target[field] = make(map[string]interface{})
		}
		for k, v := range sourceField {
			target[field].(map[string]interface{})[k] = v
		}
	}
}
