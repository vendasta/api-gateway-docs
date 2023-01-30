package com.example.vendasta.ShoeStore.configuration;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class JSONConfiguration {

    @Value("${apigateway.marketplace-private-key}")
    protected String marketPlacePrivateKey;
    
    @Bean("objectMapper")
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    @Bean("privateKeyReader")
    Reader privateKeyReader() throws StreamReadException, DatabindException, IOException{
		File file = ResourceUtils.getFile(marketPlacePrivateKey);
        String privateKey = new String(Files.readAllBytes(file.toPath()));
        Reader inputString = new StringReader(privateKey);
        return inputString;
	}

}
