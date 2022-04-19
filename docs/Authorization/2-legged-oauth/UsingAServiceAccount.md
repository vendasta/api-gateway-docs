# Obtaining an Access Token for a service account

<!-- theme: info -->
> These directions assume you have already [created a service account](CreatingAServiceAccount.md)

## Command Line
It is generally best to use a library (below) within the same code that you will
be writing the rest of your application in. For debugging and using the try it now
features of this site you may use our [command line tool](https://github.com/vendasta/api-gateway-docs/tree/master/examples/go/access-token).

## Using a library
We recommend using one of the OAuth2 libraries listed at [jwt.io](https://jwt.io/) to generate your access tokens. Look for one in your preferred programming language that has a green checkmark next to `Sign` and `RS256`.

### Parameters
Most of the parameters that you need to pass into the library can be found in the json file that was downloaded when you created keys for your service account. As a reminder here is what that file looks like:

```json
{
  "type": "service_account",
  "private_key_id": "c0273fce-79b7-4104-8a8c-ea489abb3979",
  "private_key": "-----BEGIN RSA PRIVATE KEY-----<private-key>-----END RSA PRIVATE KEY-----\n",
  "client_email": "automated-account-creation@partner-service-account.apigateway.co",
  "token_uri": "https://sso-api-prod.apigateway.co/oauth2/token",
  "assertionHeaderData": {
    "alg": "RS256",
    "kid": "c0273fce-79b7-4104-8a8c-ea489abb3979"
  },
  "assertionPayloadData": {
    "aud": "https://iam-prod.apigateway.co",
    "iss": "automated-account-creation@partner-service-account.apigateway.co",
    "sub": "automated-account-creation@partner-service-account.apigateway.co"
  }
}
```

The exact format for providing the parameters will change for each library. Generally, they are broken into `assertion header` and `assertion payload` categories.

#### Assertion Headers Parameters
- **alg** (AKA Algorithm): Use the value found at the `alg` key in your downloaded JSON file's `assertionHeaderData`.
- **kid** (AKA Key ID): Use the value found at the `kid` key in your downloaded JSON file's `assertionHeaderData`.


#### Assertion Payload Parameters

- **aud** (AKA audience): Use the value found at the `aud` key in your downloaded JSON file's `assertionPayloadData`.
- **iat** (AKA issued at): The current time as the number of **seconds**
  since the [Unix Epoch](https://en.wikipedia.org/wiki/Unix_time). Most languages include helpers for calculating this.
- **exp** (AKA expiry): We recommend a value of 10 minutes (or less) from the current time. The **exp** parameter defines the point at which the request to create an access token is no longer valid. This protects against replay attacks. If your library does not have a helper it should be specified as the number of **seconds**
  since the [Unix Epoch](https://en.wikipedia.org/wiki/Unix_time).

- **iss** (AKA issuer): Use the value found at the `iss` key in your downloaded JSON file's `assertionPayloadData`.
- **sub** (AKA subject): Use the value found at the `sub` key in your downloaded JSON file's `assertionPayloadData`.
- **scope**: This parameter should contain a space-separated list of the categories of things you would like the access token to be able to use. Each of the API operations provides a list of scopes. At least one of them must be included here to call that endpoint.


### Examples

Configuring your OAuth2 library will vary, however here are some example implementations which obtains an access token and uses it to call Vendasta's UserInfo endpoint.

**Note:** these examples assumes you've included your client credential key pair in the current working directory at the path: `./client-credentials.json`.


<!--
type: tab
title: Golang
-->
```go
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"strings"
	"time"

	"golang.org/x/oauth2/jwt"
)

type credentials struct {
	Type         string `json:"type"`
	PrivateKeyID string `json:"private_key_id"`
	PrivateKey   string `json:"private_key"`
	ClientEmail  string `json:"client_email"`
	TokenURI     string `json:"token_uri"`
	AssertionPayloadData struct {
		Aud string `json:"aud"`
		Iss string `json:"iss"`
		Sub string `json:"sub"`
	} `json:"assertionPayloadData"`
}

func main() {
	// Read in the downloaded Credential JSON
	bytes, err := ioutil.ReadFile("./client-credentials.json")
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
		PrivateKeyID: creds.PrivateKeyID,
		// The JWT's subject is the service account's email
		Subject: creds.ClientEmail,
		// A list of scopes which the Access Token will have access to.
		// We'll request the 'profile' and 'email' scopes
		Scopes: []string{"profile", "email"},
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

	// Create a Test Request to Vendasta's UserInfo endpoint
	body := strings.NewReader("")
	req, err := http.NewRequest("GET", "https://sso-api-prod.apigateway.co/oauth2/user-info", body)
	if err != nil {
		log.Fatal(err)
		return
	}

	// Attach our access token using the Authorization header.
	req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", tokenInfo.AccessToken))

	// Perform the request
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		log.Fatal(err)
		return
	}

	// Receive and print out the response
	respBytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatal(err)
		return
	}

	fmt.Println(string(respBytes))
}
```

<!--
type: tab
title: C#
-->
```csharp
using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Threading.Tasks;
using System.Web;
using Jose;
using Newtonsoft.Json;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.OpenSsl;
using Org.BouncyCastle.Security;

namespace csharp
{
    // Object representing our client credentials JSON file
    internal class ClientCredentials
    {
        [JsonProperty("type")]
        public string Type { get; set; }

        [JsonProperty("assertionHeaderData")]
        public AssertionHeaderData AssertionHeaderData { get; set; }

        [JsonProperty("assertionPayloadData")]
        public AssertionPayloadData AssertionPayloadData { get; set; }

        [JsonProperty("private_key_id")]
        public Guid PrivateKeyId { get; set; }

        [JsonProperty("private_key")]
        public string PrivateKey { get; set; }

        [JsonProperty("client_email")]
        public string ClientEmail { get; set; }

        [JsonProperty("token_uri")]
        public Uri TokenUri { get; set; }
    }

    internal class AssertionHeaderData
    {
        [JsonProperty("alg")]
        public string Alg { get; set; }

        [JsonProperty("kid")]
        public Guid Kid { get; set; }
    }

    internal class AssertionPayloadData
    {
        [JsonProperty("aud")]
        public Uri Aud { get; set; }

        [JsonProperty("iss")]
        public string Iss { get; set; }

        [JsonProperty("sub")]
        public string Sub { get; set; }
    }

    public static class Program
    {
        static async Task Main(string[] args)
        {
            try
            {
                // Load client-credentials json file
                using var r = new StreamReader("client-credentials.json");
                var jsonString = await r.ReadToEndAsync();
                var credentials = JsonConvert.DeserializeObject<ClientCredentials>(jsonString);
                var assertion = BuildClientAssertion(credentials);
                var client = new HttpClient();
                var accessToken = await ExchangeAssertionsForToken(client, credentials.TokenUri, assertion);
                // Use the access token to access a Vendasta API
                await GetUserInfo(client, accessToken);
                Console.WriteLine(accessToken);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error");
                Console.WriteLine(ex.ToString());
            }
        }

        private static string BuildClientAssertion(ClientCredentials credentials)
        {
            var now = DateTimeOffset.UtcNow;
            // Build the headers for our assertion jwt
            var headers = new Dictionary<string, object>
            {
                {"kid", credentials.assertionHeaderData.kid},
                {"alg", credentials.assertionHeaderData.alg},
            };

            // Build the payload for our assertion jwt
            var payload = new Dictionary<string, object>
            {
                {"aud", credentials.assertionPayloadData.aud},
                {"iat", now.ToUnixTimeSeconds()},
                {"exp", now.AddMinutes(10).ToUnixTimeSeconds()},
                {"iss", credentials.assertionPayloadData.iss},
                {"sub", credentials.assertionPayloadData.sub},
                {"scope", "profile email"},
            };
            // Parse our PrivateKey PEM
            var keyPair =
                (AsymmetricCipherKeyPair) new PemReader(new StringReader(credentials.PrivateKey)).ReadObject();
            var rsaParams = DotNetUtilities.ToRSAParameters((RsaPrivateCrtKeyParameters) keyPair.Private);
            var csp = new RSACryptoServiceProvider();
            csp.ImportParameters(rsaParams);
            var assertions = Jose.JWT.Encode(payload, csp, JwsAlgorithm.RS256, headers);
            return assertions;
        }

        static async Task<string> ExchangeAssertionsForToken(HttpClient client, string tokenUri, string assertions){
            // Build our form body
            var formData = HttpUtility.ParseQueryString(string.Empty);
            formData.Add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
            formData.Add("assertion", assertions);
            var postContent = new StringContent(formData.ToString());
            postContent.Headers.ContentType = MediaTypeHeaderValue.Parse("application/x-www-form-urlencoded");

            // Exchange assertion for access token via tokenUri
            var response = await client.PostAsync(tokenUri, postContent);
            var responseString = await response.Content.ReadAsStringAsync();
            var responseObject = JsonConvert.DeserializeObject<Dictionary<string, string>>(responseString);
            // Extract the access token from the response and return it
            return responseObject["access_token"];
        }

        static async Task GetUserInfo(HttpClient client, string accessToken)
        {
            var message = new HttpRequestMessage(HttpMethod.Get, "https://sso-api-prod.apigateway.co/oauth2/user-info");
            // Use our Access Token as Authorization
            message.Headers.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);
            var response = client.SendAsync(message);
            var userInfoString = await response.Result.Content.ReadAsStringAsync();
            // Print out the results of the user info endpoint
            Console.WriteLine(userInfoString);
        }
    }
}
```

<!--
type: tab
title: Java
-->
```java
package oauth2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceAccountSample {

    private static AccessToken exchangeWithJWTBearer() {
        JSONParser jsonParser = new JSONParser();
        JSONObject credentials;
        try {
            FileReader credentialsReader = new FileReader("client-credentials.json");
            //Read JSON file
            credentials = (JSONObject) jsonParser.parse(credentialsReader);

            String privateKeyContent = (String) credentials.get("private_key");

            // Adds BouncyCaste security provider, registers itself under the name "BC"
            Security.addProvider(new BouncyCastleProvider());
            // Create reader of the private key string to feed into BouncyCastle PEMParser
            StringReader privateReader = new StringReader(privateKeyContent);
            PEMParser pemParser = new PEMParser(privateReader);
            // BC is the previously registered BouncyCastle security provider
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            // Parse the PEMKeyPair
            Object object = pemParser.readObject();
            KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
            // Extract the private and public keys
            RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
            // Done with the reader; close it
            privateReader.close();

            // Make RSA256 algorithm with public + private RSA keys
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

            // Form and sign the JWT
            Map<String, Object> headerClaims = new HashMap<String, Object>();
            JSONObject jsonObject = (JSONObject)credentials.get("assertionHeaderData");
            headerClaims.put("kid", jsonObject.get("kid"));
            headerClaims.put("alg", jsonObject.get("alg"));

            // jti claim is not yet used by Vendasta, and it will be ignored in the backend; regardless, here is an example of how to make and use the claim
            String jti = UUID.randomUUID().toString();
            jsonObject = (JSONObject)credentials.get("assertionPayloadData");

            String token = JWT.create()
                    .withAudience((String) jsonObject.get("aud"))
                    .withIssuer((String) jsonObject.get("iss"))
                    .withSubject((String) jsonObject.get("sub"))
                    .withHeader(headerClaims)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(10 * 60))) // 10 minutes in the future
                    .withJWTId(jti) // not used currently
                    .sign(algorithm);

            JWTBearerGrant bearerGrant = new JWTBearerGrant(SignedJWT.parse(token));

            // The request scope for the token
            Scope scope = new Scope("profile", "email");
            // The token endpoint
            URI tokenEndpoint = new URI((String)credentials.get("token_uri"));
            // Make the token request
            TokenRequest request = new TokenRequest(tokenEndpoint, bearerGrant, scope);
            TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());
            if (!response.indicatesSuccess()) {
                // We got an error response...
                TokenErrorResponse errorResponse = response.toErrorResponse();
                System.out.println(errorResponse.getErrorObject().toString());
                return null;
            }

            AccessTokenResponse successResponse = response.toSuccessResponse();

            // Get the access token, the server may also return a refresh token
            AccessToken accessToken = successResponse.getTokens().getAccessToken();
            // Uncomment to get refresh token if it was returned
//            RefreshToken refreshToken = successResponse.getTokens().getRefreshToken();

            return accessToken;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (com.nimbusds.oauth2.sdk.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // https://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
    public static String executePost(String targetURL, String urlParameters, AccessToken accessToken) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/vnd.api+json");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Add access token auth to header
            connection.setRequestProperty("Authorization", accessToken.toAuthorizationHeader());

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = null;
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        AccessToken accessToken = exchangeWithJWTBearer();
        if (accessToken == null) {
            System.out.println("Failed to retrieve access token. Exiting...");
            return;
        }
        String response = executePost("https://sso-api-prod.apigateway.co/oauth2/user-info", "", accessToken);
        System.out.println(response);
    }
}

```
<!-- type: tab-end -->


## Manually
If you like working in obscure languages or doing things the hard way here is a quick overview of the process. For full details you will need to read the [OAuth2 specs](https://oauth.net/specs/) yourself.

### Building a client assertion

The assertion we'll use to prove our identity to Vendasta's token endpoint is a [JWT](https://jwt.io/introduction/).

A JWT consists of a _header_ section, a _payload_ section and a _signature_ section.

#### Building the assertion header

The header of your assertion must contain both the algorithm (`alg`) with which the JWT is signed, and the id of the private key used to sign it (`kid`).

Both of these values may be found within the service account credential JSON file you downloaded in a previous step. Here's an example JWT header prior to encoding:

```json
{
  "alg": "RS256",
  "kid": "c0273fce-79b7-4104-8a8c-ea489abb3979"
}
```

Here's where to find each piece:

- **alg**: Use the value found at the `alg` key in your downloaded JSON file's `assertionHeaderData`.
- **kid**: Use the value found at the `kid` key in your downloaded JSON file's `assertionHeaderData`.


#### Building the assertion payload

Now we must build the assertion's payload. Here's an example payload prior to encoding.

```json
{
  "aud": "https://iam-prod.apigateway.co",
  "iat": 1591287394,
  "exp": 1591287994,
  "iss": "automated-account-creation@partner-service-account.apigateway.co",
  "sub": "automated-account-creation@partner-service-account.apigateway.co",
  "scope": "profile email"
}
```

Here's where to find each piece:

- **aud** (AKA audience): Use the value found at the `aud` key in your downloaded JSON file's `assertionPayloadData`.
 - **iat** (AKA issued at): The time the jwt was issued as the number of **seconds**
  since the [Unix Epoch](https://en.wikipedia.org/wiki/Unix_time). Most languages include helpers for calculating this.</dd>
- **exp** (AKA expiry): The time when the token should expire as the number of **seconds**
  since the [Unix Epoch](https://en.wikipedia.org/wiki/Unix_time).
  We recommend a value of 10 minutes (or less) from when the token was issued.</dd>
- **iss** (AKA issuer): Use the value found at the `iss` key in your downloaded JSON file's `assertionPayloadData`.
- **sub** (AKA subject): Use the value found at the `sub` key in your downloaded JSON file's `assertionPayloadData`.
- **scope**: Space-separated [scopes](https://tools.ietf.org/html/rfc6749#section-3.3) which should be included on the access token which will be issued. Each of the endpoints will provide a list of scopes. At least one of them must be included here in order to call that endpoint.

**Note:** The `jti` claim, which is a client-provided unique identifier for the JWT (typically a UUID) is not yet supported by Vendasta. It will be ignored if provided.

After constructing a JSON object representing your claims, you'll need to sign it with your private key.

#### Signing the client assertion

The steps required to sign your JWT will vary substantially depending on which
language you are working with. Please consult a trusted JWT library in the language of your choice.

Ensure you provide the appropriate headers, use the correct signing algorithm (as specified in your downloaded JSON file) and provide your payload as the JWT's claims.

The encoded and signed JWT you receive as a result of this process will then be used as your client **assertion** in the next step.

### Exchanging your client assertion for an access token

Now that we have a signed client assertion we can provide it to Vendasta's **token URL** to receive an **access token**.

We need to provide this assertion as a parameter to the **token URL**.

Use your language's provided library to encode the following key-value pairs as form data and set your `Content-Type` header to `application/x-www-form-urlencoded`.

* `grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer`
* `assertion=<client-assertion>` (use the client assertion you built in the previous step)

Construct a `POST` request to URI provided in the `token_uri` field of your downloaded JSON file with the encoded form-data as the body.

Here's an example `curl` command:

```sh
curl --location --request POST 'https://sso-api-prod.apigateway.co/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer' \
--data-urlencode 'assertion=<your-signed-assertion>'
```
Note that the `<your-signed-assertion>` should be replaced with the encrypted set of characters created by signing the json assertion.
