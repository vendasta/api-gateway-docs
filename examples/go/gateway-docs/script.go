package main

//
//import (
//	"fmt"
//	"github.com/getkin/kin-openapi/openapi2"
//	"github.com/getkin/kin-openapi/openapi2conv"
//	"github.com/getkin/kin-openapi/openapi3"
//
//	"github.com/oasdiff/yaml"
//	"log"
//	"os"
//	"path/filepath"
//)
//
//const inputDir = "./vendastaapisgen"
//const outputFile = "./openapi/grpc/grpc_api.swagger.yaml"
//
//func main() {
//	fmt.Println("🔍 Extracting OpenAPI YAML files...")
//	files := extractFiles(inputDir)
//
//	if len(files) == 0 {
//		log.Fatalf("❌ No OpenAPI files found in %s", inputDir)
//	}
//
//	fmt.Println("🔄 Converting Swagger 2.0 files to OpenAPI 3.0...")
//	specs := convertToOpenAPI3(files)
//
//	fmt.Println("🔗 Merging OpenAPI specs...")
//	mergedSpec := mergeSpecs(specs)
//
//	fmt.Println("💾 Writing merged OpenAPI spec to:", outputFile)
//	if err := writeMergedSpec(outputFile, mergedSpec); err != nil {
//		log.Fatalf("❌ Error writing merged file: %v", err)
//	}
//
//	fmt.Println("✅ Merging completed successfully!")
//}
//
//// extractFiles finds all `api.swagger.yaml` files in the given directory
//func extractFiles(dir string) []string {
//	files, err := filepath.Glob(filepath.Join(dir, "**/api.swagger.yaml"))
//	if err != nil {
//		log.Fatalf("❌ Error reading YAML files: %v", err)
//	}
//	return files
//}
//
////	func convertToOpenAPI3(files []string) []*openapi3.T {
////		var specs []*openapi3.T
////		loader := openapi3.NewLoader()
////
////		for _, file := range files {
////			fmt.Println("Processing:", file)
////
////			// Load OpenAPI file
////			doc, err := loader.LoadFromFile(file)
////			if err != nil {
////				log.Printf("⚠️ Skipping %s due to error: %v\n", file, err)
////				continue
////			}
////
////			// Convert Swagger 2.0 → OpenAPI 3.0 if needed
////			if doc.OpenAPI == "" && doc.Swagger == "2.0" {
////				fmt.Println("🔄 Converting Swagger 2.0 to OpenAPI 3.0:", file)
////
////				swaggerDoc, err := loader.LoadSwaggerFromFile(file)
////				if err != nil {
////					log.Printf("⚠️ Skipping %s due to conversion error: %v\n", file, err)
////					continue
////				}
////
////				doc, err = openapi2conv.ToV3(swaggerDoc)
////				if err != nil {
////					log.Printf("⚠️ Skipping %s due to conversion failure: %v\n", file, err)
////					continue
////				}
////			}
////
////			specs = append(specs, doc)
////		}
////		return specs
////	}
//func convertToOpenAPI3(files []string) []*openapi3.T {
//	var specs []*openapi3.T
//	loader := openapi3.NewLoader()
//
//	for _, file := range files {
//		fmt.Println("Processing:", file)
//
//		// Load OpenAPI file
//		doc, err := loader.LoadFromFile(file)
//		if err != nil {
//			log.Printf("⚠️ Skipping %s due to error: %v\n", file, err)
//			continue
//		}
//
//		// Check if it's a Swagger 2.0 file
//		if isSwagger2(file) {
//			fmt.Println("🔄 Converting Swagger 2.0 to OpenAPI 3.0:", file)
//
//			swaggerDoc, err := openapi2.NewLoader().LoadFromFile(file)
//			if err != nil {
//				log.Printf("⚠️ Skipping %s due to conversion error: %v\n", file, err)
//				continue
//			}
//
//			doc, err = openapi2conv.ToV3(swaggerDoc)
//			if err != nil {
//				log.Printf("⚠️ Skipping %s due to conversion failure: %v\n", file, err)
//				continue
//			}
//		}
//
//		specs = append(specs, doc)
//	}
//	return specs
//}
//func writeMergedSpec(outputPath string, spec *openapi3.T) error {
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
//
//func mergeSpecs(specs []*openapi3.T) *openapi3.T {
//	mergedSpec := &openapi3.T{
//		OpenAPI:    "3.0.3",
//		Info:       &openapi3.Info{Title: "Merged API", Version: "1.0.0"},
//		Paths:      openapi3.NewPaths(),
//		Components: &openapi3.Components{},
//	}
//
//	for _, spec := range specs {
//		// Merge paths
//		for path, item := range spec.Paths {
//			mergedSpec.Paths[path] = item
//		}
//
//		// Merge components
//		if spec.Components != nil {
//			if mergedSpec.Components.Schemas == nil {
//				mergedSpec.Components.Schemas = make(map[string]*openapi3.SchemaRef)
//			}
//			for name, schema := range spec.Components.Schemas {
//				mergedSpec.Components.Schemas[name] = schema
//			}
//		}
//	}
//	return mergedSpec
//}
//func isSwagger2(file string) bool {
//	loader := openapi3.NewLoader()
//	doc, err := loader.LoadFromFile(file)
//	if err != nil {
//		return false
//	}
//	return doc.OpenAPI == "" // Swagger 2.0 files don't have an "openapi" field
//}
//func mergeSwagger2(files []string) *openapi2.T {
//	mergedSpec := &openapi2.T{
//		Swagger: "2.0",
//		Info: &openapi2.Info{
//			Title:   "Merged API",
//			Version: "1.0.0",
//		},
//		Paths:       make(openapi2.Paths),
//		Definitions: make(openapi2.Definitions),
//	}
//
//	loader := openapi2.NewLoader()
//
//	for _, file := range files {
//		fmt.Println("Processing:", file)
//
//		// Load Swagger 2.0 file
//		doc, err := loader.LoadFromFile(file)
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
