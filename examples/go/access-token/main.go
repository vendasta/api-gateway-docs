// This script can be used to generate a two legged access token
//
// You will need a json credentials file with an `alg` of `RS256`.
// To create a new file go to Partner Center -> Administration -> Service Accounts -> Manage Keys -> "Generate Private Key (RS256)"
//
// go run main.go <private key file path> [scope1 [scope2]]
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"time"

	"golang.org/x/oauth2/jwt"
)

// This json file will be loaded into this struct
type credentials struct {
	Type                string `json:"type"`
	PrivateKeyID        string `json:"private_key_id"`
	PrivateKey          string `json:"private_key"`
	ClientEmail         string `json:"client_email"`
	TokenURI            string `json:"token_uri"`
	AssertionHeaderData struct {
		Alg string `json:"alg"`
		Kid string `json:"kid"`
	} `json:"assertionHeaderData"`
	AssertionPayloadData struct {
		Aud string `json:"aud"`
		Iss string `json:"iss"`
		Sub string `json:"sub"`
	} `json:"assertionPayloadData"`
}

func main() {
	verbose := false

	//Set some defaults
	credentialFile := "./client-credentials.json"
	scopes := []string{"business order user.admin"}

	// Load the values from the command line
	if len(os.Args) >= 3 {
		credentialFile = os.Args[1]
		scopes = os.Args[2:]
	}

	if verbose {
		fmt.Printf("Generating for file: %v\n", credentialFile)
		fmt.Printf("Generating with scopes: %v\n", scopes)
	}

	// Read in the downloaded Credential JSON
	bytes, err := ioutil.ReadFile(credentialFile)
	if err != nil {
		log.Fatal(err)
		return
	}

	// Parse the JSON credential to access its fields
	var creds credentials
	err = json.Unmarshal(bytes, &creds)
	if err != nil {
		log.Fatal(err)
		return
	}

	// Construct the configuration required by the "golang.org/x/oauth2/jwt" library
	oauthConfig := jwt.Config{
		// The email address of our service account
		Email: creds.ClientEmail,
		// The private key we created
		PrivateKey: []byte(creds.PrivateKey),
		// The ID of the private key we created
		PrivateKeyID: creds.AssertionHeaderData.Kid,
		// The JWT's subject is the service account's email
		Subject: creds.AssertionPayloadData.Sub,
		// A list of scopes which the Access Token will have access to.
		Scopes: scopes,
		// The provided token URL
		TokenURL: creds.TokenURI,
		// Expire the Access Token in 10 minutes
		Expires: 10 * time.Minute,
		// The audience of the JWT
		Audience: creds.AssertionPayloadData.Aud,
		// We don't need to provide any private claims
		PrivateClaims: nil,
		// We need an Access Token, not an ID Token
		UseIDToken: false,
	}

	// Fetch a token from Vendasta using a client assertion
	ctx := context.Background()
	tokenInfo, err := oauthConfig.TokenSource(ctx).Token()
	if err != nil {
		log.Fatal(err)
		return
	}

	if verbose {
		fmt.Printf("Expires at %v\n", tokenInfo.Expiry.Format(time.Kitchen))
	}
	fmt.Printf("Bearer %s\n", tokenInfo.AccessToken)
}
