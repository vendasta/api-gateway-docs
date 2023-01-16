package com.example.vendasta.ShoeStore.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.vendasta.ShoeStore.entity.marketplace.MarketPlaceOathTokenRequest;
import com.example.vendasta.ShoeStore.entity.marketplace.OathTokenResponse;
import com.example.vendasta.ShoeStore.utils.AESUtils;
import com.example.vendasta.ShoeStore.utils.ParserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import java.io.IOException;
import java.io.Reader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Configuration
public class JWTGen {
    private static final Logger logger = LoggerFactory.getLogger(JWTGen.class);

    @Value("${apigateway.app-id}")
    protected String appId;

    @Value("${apigateway.aesPassword}")
    protected String pass;

    @Value("${apigateway.aesSalt}")
    protected String salt;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    Reader privateKeyReader;

    @Autowired
    IvParameterSpec iv;

    public String marketPlaceJwtToken() throws IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String token;
        String accessToken;
        int expires;

        RSAPrivateKey privRSA = (RSAPrivateKey) PemUtils.readPKCS1PrivateKeyFromFile(privateKeyReader, "RSA");
        Algorithm algorithm = Algorithm.RSA256(null, privRSA); // only the private key is used for signing
        token = JWT.create()
                .withIssuer(appId)
                .withClaim("iat", Instant.now().getEpochSecond())
                //Keeping the expiry time to 59 minutes because we have a 1 hour limit for expiry time
                //https://developers.vendasta.com/vendor/ZG9jOjIxNzM0NjA4-api-authentication
                .withClaim("exp", Instant.now().plusSeconds(60).getEpochSecond())
                .sign(algorithm);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Type", "application/json");

        List<MediaType> messageConverters = new ArrayList<>();
        messageConverters.add(MediaType.TEXT_PLAIN);
        headers.setAccept(messageConverters);
        MarketPlaceOathTokenRequest body = new MarketPlaceOathTokenRequest("urn:ietf:params:oauth:grant-type:jwt-bearer", token);
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<OathTokenResponse> response = restTemplate.exchange("https://developers.vendasta.com/api/v1/oauth/token", HttpMethod.POST, httpEntity, OathTokenResponse.class);

        if (response.hasBody() && response.getStatusCode().equals(HttpStatus.OK) && !Objects.isNull(response.getBody())) {
            accessToken = response.getBody().data.access_token;
            expires = response.getBody().data.expires;
        } else {
            throw new HttpServerErrorException(response.getStatusCode());
        }
        return accessToken;
    }


    // Use this method to generate a new access-token based on the validity of the current access-token.

    // public String getRefreshToken() {
    //     try{
    //         // token = previously generated marketplace access token;
    //         Instant i = Instant.EPOCH.plus(token.getExpires(), ChronoUnit.SECONDS);

    //         //System.out.println("regenerate market place token:"+ token.getExpires());
    //         if(ParserUtils.isTokenExpired(i)){
    //         //   logger.info("Token has expired. Need to generate again " + i);
    //             return marketPlaceJwtToken();
    //         }return AESUtils.decryptWithPassword(token.getTokenId(), pass, salt, iv);
    //     }catch (IOException | InvalidAlgorithmParameterException | IllegalBlockSizeException | NoSuchPaddingException |
    //             NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException | InvalidKeyException e){
    //         return null;
    //     }
    //     return null;
    // }


}
