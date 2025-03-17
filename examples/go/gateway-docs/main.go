package main

import (
	"fmt"
	"github.com/getkin/kin-openapi/openapi2"
	yaml "gopkg.in/yaml.v3"
	"log"
	"os"
	"path/filepath"
)

//const inputDir = "./vendastaapisgen" // Adjust based on actual workspace
//const outputFile = "./openapi/grpc/grpc_api.swagger.yaml"

// func main() {
//
//		mergedSpec := &openapi3.T{
//			OpenAPI: "3.0.0",
//			Info: &openapi3.Info{
//				Title:   "Merged API",
//				Version: "1.0.0",
//			},
//			Paths: openapi3.NewPaths(),
//		} // Create an empty OpenAPI spec
//
//		// Walk through the directories and process api.swagger.yaml files
//		err := filepath.Walk(inputDir, func(path string, info os.FileInfo, err error) error {
//			if err != nil {
//				return err
//			}
//			if info.IsDir() || info.Name() != "api.swagger.yaml" {
//				return nil
//			}
//
//			fmt.Println("Processing:", path)
//
//			// Load and validate OpenAPI spec
//			loader := openapi3.NewLoader()
//			doc, err := loader.LoadFromFile(path)
//			if err != nil {
//				log.Printf("Error parsing %s: %v\n", path, err)
//				return nil // Skip invalid files
//			}
//
//			// Merge paths into final spec
//			for k, v := range doc.Paths.Map() {
//				mergedSpec.Paths.Set(k, v)
//			}
//
//			return nil
//		})
//
//		if err != nil {
//			log.Fatalf("Error scanning directories: %v", err)
//		}
//
//		// Write the final merged OpenAPI YAML
//		if err := writeMergedSpec(outputFile, mergedSpec); err != nil {
//			log.Fatalf("Error writing merged file: %v", err)
//		}
//
//		fmt.Println("Merged OpenAPI spec saved to:", outputFile)
//	}
//func main() {
//	mergedSpec := &openapi3.T{
//		OpenAPI: "3.0.3",
//		Info: &openapi3.Info{
//			Title:   "Merged API",
//			Version: "1.0.0",
//		},
//		Paths: openapi3.NewPaths(),
//		//Paths: make(openapi3.Paths),
//		//Paths: &openapi3.Paths{Make: make(map[string]*openapi3.PathItem)},
//	}
//
//	// Get all OpenAPI YAML files
//	files, err := filepath.Glob(filepath.Join(inputDir, "**/api.swagger.yaml"))
//	if err != nil {
//		log.Fatalf("Error reading YAML files: %v", err)
//	}
//
//	for _, file := range files {
//		fmt.Println("Merging:", file)
//
//		loader := openapi3.NewLoader()
//		doc, err := loader.LoadFromFile(file)
//		if err != nil {
//			log.Printf("Skipping %s due to error: %v\n", file, err)
//			continue
//		}
//
//		// Merge paths
//		for path, item := range doc.Paths.Map() {
//			//mergedSpec.Paths[path] = item
//			mergedSpec.Paths.Set(path, item)
//		}
//	}
//
//	// Write the final merged OpenAPI YAML
//	if err := writeMergedSpec1(outputFile, mergedSpec); err != nil {
//		log.Fatalf("Error writing merged file: %v", err)
//	}
//
//	fmt.Println("✅ Merged OpenAPI spec saved to:", outputFile)
//}
//
//func writeMergedSpec1(outputPath string, spec *openapi3.T) error {
//	outputDir := filepath.Dir(outputPath)
//	if err := os.MkdirAll(outputDir, os.ModePerm); err != nil {
//		return fmt.Errorf("failed to create output directory: %v", err)
//	}
//
//	yamlData, err := yaml.Marshal(spec)
//	if err != nil {
//		return err
//	}
//	return os.WriteFile(outputPath, yamlData, 0644)
//}

const inputDir = "./vendastaapisgen" // Update with actual directory
const outputFile = "./openapi/grpc/grpc_api.swagger.yaml"

type SwaggerInfo struct {
	Title   string `yaml:"title"`
	Version string `yaml:"version"`
}

//func main() {
//	fmt.Println("🔍 Finding Swagger 2.0 YAML files...")
//	files, err := findSwaggerFiles(inputDir)
//	if err != nil {
//		log.Fatalf("Error finding files: %v", err)
//	}
//
//	if len(files) == 0 {
//		log.Fatalf("No Swagger 2.0 files found in %s", inputDir)
//	}
//
//	fmt.Println("🔗 Merging Swagger 2.0 specs...")
//	mergedSpec := mergeSwagger2(files)
//
//	fmt.Println("💾 Writing merged Swagger 2.0 spec to:", outputFile)
//	if err := writeMergedSpec(outputFile, mergedSpec); err != nil {
//		log.Fatalf("Error writing merged file: %v", err)
//	}
//
//	fmt.Println("Merging completed successfully!")
//}
//
//// findSwaggerFiles finds all `api.swagger.yaml` files in the directory
//func findSwaggerFiles(dir string) ([]string, error) {
//	files, err := filepath.Glob(filepath.Join(dir, "**/api.swagger.yaml"))
//	if err != nil {
//		return nil, err
//	}
//	return files, nil
//}
//
//// mergeSwagger2 loads and merges multiple Swagger 2.0 specs
//func mergeSwagger2(files []string) *openapi2.T {
//	mergedSpec := &openapi2.T{
//		Swagger: "2.0",
//		Info: openapi3.Info{
//			Title:   "Merged API",
//			Version: "1.0.0",
//		},
//		Paths:       make(map[string]*openapi2.PathItem),
//		Definitions: make(map[string]*openapi2.SchemaRef),
//	}
//
//	for _, file := range files {
//		fmt.Println("Processing:", file)
//
//		// Load Swagger 2.0 file
//		doc := &openapi2.T{}
//		err := loadSwaggerFile(file, doc)
//		if err != nil {
//			log.Printf("⚠️ Skipping %s due to error: %v\n", file, err)
//			continue
//		}
//
//		// Merge paths
//		for path, item := range doc.Paths {
//			mergedSpec.Paths[path] = item
//		}
//
//		// Merge definitions
//		for name, schema := range doc.Definitions {
//			mergedSpec.Definitions[name] = schema
//		}
//	}
//
//	return mergedSpec
//}
//
//func loadSwaggerFile(filePath string, doc *openapi2.T) error {
//	yamlFile, err := os.ReadFile(filePath)
//	if err != nil {
//		return err
//	}
//
//	err = yaml.Unmarshal(yamlFile, doc)
//	if err != nil {
//		return err
//	}
//	return nil
//}
//
//// writeMergedSpec saves the merged Swagger 2.0 YAML file
//func writeMergedSpec(outputPath string, spec *openapi2.T) error {
//	outputDir := filepath.Dir(outputPath)
//	if err := os.MkdirAll(outputDir, os.ModePerm); err != nil {
//		return fmt.Errorf("failed to create output directory: %v", err)
//	}
//
//	yamlData, err := yaml.Marshal(spec)
//	if err != nil {
//		return err
//	}
//	return os.WriteFile(outputPath, yamlData, 0644)
//}

func main() {
	// Create an empty Swagger 2.0 spec
	mergedSpec := &openapi2.T{
		Swagger: "2.0",
		Paths:   make(map[string]*openapi2.PathItem),
	}

	// Walk through the directories and process api.swagger.yaml files
	err := filepath.Walk(inputDir, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() || info.Name() != "api.swagger.yaml" {
			return nil
		}

		fmt.Println("Processing:", path)

		// Load Swagger 2.0 spec
		doc, err := loadSwaggerFile(path)
		if err != nil {
			log.Printf("Skipping %s (failed to load): %v\n", path, err)
			return nil // Skip invalid files
		}

		// Merge paths with nil check
		for k, v := range doc.Paths {
			if v == nil {
				log.Printf("Skipping nil path: %s\n", k)
				continue
			}
			mergedSpec.Paths[k] = v
		}

		return nil
	})

	if err != nil {
		log.Fatalf("Error scanning directories: %v", err)
	}

	//// Write merged file
	//if err := writeMergedSpec(outputFile, mergedSpec); err != nil {
	//	log.Fatalf("Error writing merged file: %v", err)
	//}

	// Write merged YAML with nil check
	if err := writeMergedSpec(outputFile, mergedSpec); err != nil {
		log.Fatalf("Error writing merged spec: %v", err)
	}

	fmt.Println("Merged Swagger 2.0 spec saved to:", outputFile)
}

// Load a Swagger 2.0 file
func loadSwaggerFile(filePath string) (*openapi2.T, error) {
	data, err := os.ReadFile(filePath)
	if err != nil {
		return nil, err
	}

	var spec openapi2.T
	if err := yaml.Unmarshal(data, &spec); err != nil {
		return nil, err
	}

	// Nil check: Ensure SchemaRef is not nil
	if spec.Paths == nil {
		spec.Paths = make(map[string]*openapi2.PathItem)
	}

	return &spec, nil
}

// Write merged spec to YAML file
func writeMergedSpec(outputPath string, spec *openapi2.T) error {
	outputDir := filepath.Dir(outputPath)
	if err := os.MkdirAll(outputDir, os.ModePerm); err != nil {
		return fmt.Errorf("failed to create output directory: %v", err)
	}

	yamlData, err := yaml.Marshal(spec)
	if err != nil {
		return fmt.Errorf("failed to marshal YAML: %v", err)
	}
	return os.WriteFile(outputPath, yamlData, 0644)
}
