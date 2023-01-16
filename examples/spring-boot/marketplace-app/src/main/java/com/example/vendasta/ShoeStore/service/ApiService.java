package com.example.vendasta.ShoeStore.service;

import com.example.vendasta.ShoeStore.configuration.JWTGen;
import com.example.vendasta.ShoeStore.entity.business.BusinessLocationData;
import com.example.vendasta.ShoeStore.entity.business.BusinessLocations;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JWTGen jwtGen;

    public BusinessLocations fetchBusinessLocations(String uri) {
        BusinessLocations businessLocations = new BusinessLocations();
        try {  
            HttpEntity request = new HttpEntity(null);
            ResponseEntity<BusinessLocations> response = restTemplate.exchange(uri, HttpMethod.GET, request, BusinessLocations.class);
            if (response.hasBody() && response.getStatusCode().equals(HttpStatus.OK) && !Objects.isNull(response.getBody())) {
                businessLocations = response.getBody();
            }
        } catch (HttpClientErrorException e) {
            // e.getMessage() For error message
        }
        return businessLocations;
	}
  
    public BusinessLocationData getBusinessLocation(String uri) {
        BusinessLocationData businessLocation = new BusinessLocationData();
        try {  
            HttpEntity request = new HttpEntity(null);
            ResponseEntity<BusinessLocationData> response = restTemplate.exchange(uri, HttpMethod.GET, request, BusinessLocationData.class);
            if (response.hasBody() && response.getStatusCode().equals(HttpStatus.OK) && !Objects.isNull(response.getBody())) {
                businessLocation = response.getBody();
            }
        } catch (HttpClientErrorException e) {
            // e.getMessage() For error message
        }
        return businessLocation;
	}

    public Boolean checkAccountAccess(String accountId,String userId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtGen.marketPlaceJwtToken());
            headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange("https://developers.vendasta.com/api/v1/user/" + userId + "/permissions/" + accountId , HttpMethod.HEAD, httpEntity, String.class);
    
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return true;
            } else {
                throw new HttpServerErrorException(response.getStatusCode());
            }
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException
                | IOException | HttpClientErrorException e) {
            if(e.getMessage().startsWith("403")) {
                return false;
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }
}