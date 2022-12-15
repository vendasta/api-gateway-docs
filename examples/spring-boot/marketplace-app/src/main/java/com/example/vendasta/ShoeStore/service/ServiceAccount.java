package com.example.vendasta.ShoeStore.service;

import com.example.vendasta.ShoeStore.entity.business.BusinessLocations;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceAccount {

    @Autowired
    RestTemplate restTemplate;

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
}