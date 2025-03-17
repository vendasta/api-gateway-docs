package main

//
//import (
//	"fmt"
//	"log"
//	"os"
//	"path/filepath"
//	"strings"
//
//	"github.com/getkin/kin-openapi/openapi2"
//	"github.com/getkin/kin-openapi/openapi2conv"
//	"gopkg.in/yaml.v3"
//)
//
//// ConvertSwaggerToOpenAPI3 converts a Swagger 2.0 file to OpenAPI 3.0
//func ConvertSwaggerToOpenAPI3(inputPath string, outputPath string) error {
//	// Read the Swagger 2.0 file
//	data, err := os.ReadFile(inputPath)
//	if err != nil {
//		return fmt.Errorf("failed to read file %s: %w", inputPath, err)
//	}
//
//	// Parse Swagger 2.0 into an OpenAPI object
//	swagger := new(openapi2.T)
//	if err := yaml.Unmarshal(data, swagger); err != nil {
//		log.Printf("⚠️ Skipping %s: Invalid YAML format (%v)\n", inputPath, err)
//		return nil // Skip and continue
//	}
//
//	// Convert to OpenAPI 3.0
//	openapi3Doc, err := openapi2conv.ToV3(swagger)
//	if err != nil {
//		log.Printf("⚠️ Skipping %s: Conversion failed (%v)\n", inputPath, err)
//		return nil // Skip and continue
//	}
//
//	// Marshal OpenAPI 3.0 into YAML
//	convertedData, err := yaml.Marshal(openapi3Doc)
//	if err != nil {
//		log.Printf("⚠️ Skipping %s: Failed to marshal OpenAPI 3.0 (%v)\n", inputPath, err)
//		return nil // Skip and continue
//	}
//
//	// Write to output file
//	if err := os.WriteFile(outputPath, convertedData, 0644); err != nil {
//		log.Printf("⚠️ Skipping %s: Failed to write file (%v)\n", outputPath, err)
//		return nil // Skip and continue
//	}
//
//	fmt.Printf("✅ Converted: %s -> %s\n", inputPath, outputPath)
//	return nil
//}
//
//// ProcessDirectory scans for Swagger 2.0 files and converts them
//func ProcessDirectory(rootDir string) error {
//	return filepath.Walk(rootDir, func(path string, info os.FileInfo, err error) error {
//		if err != nil {
//			return err
//		}
//
//		// Check if file is a Swagger 2.0 YAML file
//		if strings.HasSuffix(info.Name(), "api.swagger.yaml") {
//			outputPath := strings.Replace(path, "api.swagger.yaml", "api.openapi3.yaml", 1)
//			return ConvertSwaggerToOpenAPI3(path, outputPath)
//		}
//		return nil
//	})
//}
//
//func main() {
//	rootDir := "vendastaapisgen" // Change this if needed
//
//	fmt.Println("🚀 Starting conversion of Swagger 2.0 files to OpenAPI 3.0...")
//	if err := ProcessDirectory(rootDir); err != nil {
//		fmt.Println("❌ Error:", err)
//	} else {
//		fmt.Println("✅ Conversion completed successfully!")
//	}
//}
