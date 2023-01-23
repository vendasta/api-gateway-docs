package com.example.vendasta.ShoeStore.configuration;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.vendasta.ShoeStore.entity.AccessTokenWrapper;
import com.example.vendasta.ShoeStore.entity.service.ServiceAccountJson;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.JWTBearerGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Configuration
public class AccessTokenConfiguration {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${apigateway.service-account-json-path}")
    protected String serviceAccountJsonPath;

    @Value("${apigateway.service-account-scope}")
    protected String serviceAccountScope;

	@Bean
    RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            if(request.getURI().getHost().contains(".apigateway.co")) {
                String accessToken = checkTokenValidity(accessTokenWrapper().getToken());
                if(accessToken != null) {
                    accessTokenWrapper().setToken(accessToken);
                    request.getHeaders().setBearerAuth(accessTokenWrapper().getToken());
                } 
            }
            return execution.execute(request, body);
		})).build();
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(byteArrayHttpMessageConverter);
        messageConverters.add(converter);
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new OAuth2AccessTokenResponseHttpMessageConverter());
        return messageConverters;
    }

	String checkTokenValidity(String aToken) {
		if(aToken == null || aToken.equals("")) {
			return exchangeWithJWTBearer().getValue();
		}
		DecodedJWT jwt = JWT.decode(aToken);
		if(jwt.getExpiresAt().before(new Date())) {
			aToken = exchangeWithJWTBearer().getValue();
		}
		return aToken;
	  }

	ServiceAccountJson serviceAccountReader() throws StreamReadException, DatabindException, IOException{
		Path pathToFile = Paths.get(serviceAccountJsonPath);
		BufferedReader inputStream = new BufferedReader(new FileReader(pathToFile.toAbsolutePath().toString()));
		ServiceAccountJson data = objectMapper.readValue(inputStream,ServiceAccountJson.class);
		return data;
	}

	AccessToken exchangeWithJWTBearer() {
        try {
			ServiceAccountJson serviceAccountJson = serviceAccountReader();
            String privateKeyContent = (String) serviceAccountJson.getPrivateKey();

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
            String kid = serviceAccountJson.getAssertionHeaderData().getKid();
            String alg = serviceAccountJson.getAssertionHeaderData().getAlg();
            headerClaims.put("kid", kid);
            headerClaims.put("alg", alg);

            // jti claim is not yet used by Vendasta, and it will be ignored in the backend; regardless, here is an example of how to make and use the claim
            String jti = UUID.randomUUID().toString();

            String aud = serviceAccountJson.getAssertionPayloadData().getAud();
            String iss = serviceAccountJson.getAssertionPayloadData().getIss();
            String sub = serviceAccountJson.getAssertionPayloadData().getSub();

            String token = JWT.create()
                    .withAudience((String) aud)
                    .withIssuer((String) iss)
                    .withSubject((String) sub)
                    .withHeader(headerClaims)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60 * 30))) // 30 mins in the future
                    .withJWTId(jti) // not used currently
                    .sign(algorithm);

            JWTBearerGrant bearerGrant = new JWTBearerGrant(SignedJWT.parse(token));

            // The request scope for the token. Modify apigateway.service-account-scope in application.yml file to change default scope
            Scope scope = new Scope(serviceAccountScope);
            // The token endpoint
            // URI tokenEndpoint = (URI) credentials.get("token_uri");
            URI tokenEndpoint = new URI(serviceAccountJson.getTokenUri());
            // Make the token request
            TokenRequest request = new TokenRequest(tokenEndpoint, bearerGrant, scope);
            TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());
            if (!response.indicatesSuccess()) {
                // We got an error response...
                // TokenErrorResponse errorResponse = response.toErrorResponse();
                // System.out.println(errorResponse.getErrorObject().toString());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            AccessTokenResponse successResponse = response.toSuccessResponse();

            // Get the access token, the server may also return a refresh token
            AccessToken accessToken = successResponse.getTokens().getAccessToken();
            // Uncomment to get refresh token if it was returned
        //  RefreshToken refreshToken = successResponse.getTokens().getRefreshToken();

            return accessToken;
        } catch (IOException | java.text.ParseException  | URISyntaxException | com.nimbusds.oauth2.sdk.ParseException e) {
            e.printStackTrace();
        } 
        throw new IllegalArgumentException("Token creation failed.");
    }

AccessTokenWrapper accessTokenWrapper() {
    AccessTokenWrapper aTokenWrapper = new AccessTokenWrapper();
    aTokenWrapper.setToken(exchangeWithJWTBearer().getValue());
    return aTokenWrapper;
}
  
}
