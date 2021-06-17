# Access Token Generation
This tool can be used to generate an access token at the command line.

You will need a json credentials file with an `alg` of `RS256`.
To create a new file go to Partner Center -> Administration -> Service Accounts -> Manage Keys -> "Generate Private Key (RS256)"

When running the tool specify the path to the json file followed by the scopes that you would like included.

Example from source
```shell
go run main.go ./client-credentials.json business order user.admin
```

If you do not have golang installed on your computer you may use one of the prebuilt executables 
instead. They can be found in the bin directory.

Example using the prebuilt image for MacOS
```shell
./bin/access-token-darwin-amd64 ~/.config/api-gateway/demo-9YW9.json business order user.admin
```

## Installation
There are several ways that you can get the tool onto your computer.

1. Download a prebuilt image
   - [From github](https://github.com/vendasta/api-gateway-docs/tree/master/examples/go/access-token/bin)
2. Install using go
   - Install [golang](https://golang.org/doc/install)
   - Run `go install github.com/vendasta/api-gateway-docs/examples/go/access-token@latest`
   - Then run `access-token <private key file path> [scope1 [scope2]]`
3. Run the source code
   - Install [golang](https://golang.org/doc/install)
   - Save [main.go](https://github.com/vendasta/api-gateway-docs/blob/master/examples/go/access-token/main.go) to your computer
   - Run `go run main.go <private key file path> [scope1 [scope2]]`
