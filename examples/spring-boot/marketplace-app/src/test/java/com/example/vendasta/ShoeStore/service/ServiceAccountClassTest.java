package com.example.vendasta.ShoeStore.service;

import com.example.vendasta.ShoeStore.entity.AccessTokenWrapper;
import com.example.vendasta.ShoeStore.entity.business.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

//@RunWith(SpringRunner.class)
// @ContextConfiguration
@SpringBootTest(classes = {ServiceAccountClassTest.class, RestTemplate.class,AccessTokenWrapper.class,ApiService.class,WebApplicationContext.class})
public class ServiceAccountClassTest {

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    AccessTokenWrapper accessTokenWrapper;

    @Autowired
    ApiService serviceAccount;

    @Value("${apigateway.partner-id}")
    protected String partnerId;

    @BeforeEach
    public void setUp() {

        Links links = new Links();
        links.setFirst("first");

        BusinessLocation businessLocation = new BusinessLocation();
        businessLocation.setId("123");
        businessLocation.setType("type");
        BusinessLocations businessLocations = new BusinessLocations();
        businessLocations.setLinks(links);

        Attributes attributes = new Attributes();
        attributes.setName("Test");
        Address address = new Address();
        address.setLine1("123 Test St");
        address.setLine2("Apt 1");
        address.setCity("Test City");
        address.setCountryCode("CA");
        address.setPostalCode("T1T1T1");
        address.setRegionCode("AB");
        address.setStateCode("AB");
        attributes.setAddress(address);
        businessLocation.setAttributes(attributes);

        businessLocations.setData(new ArrayList<>(List.of(businessLocation)));

        final String uri = String.format("https://test.com", "1234", partnerId);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<BusinessLocations> myEntity = new ResponseEntity<BusinessLocations>(businessLocations, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(uri, HttpMethod.GET, request, BusinessLocations.class)).thenReturn(myEntity);
    }

    @Test
    public void testExecuteGet() {
        final String uri = String.format("https://test.com", "1234", partnerId);
        BusinessLocations businessLocations = serviceAccount.fetchBusinessLocations(uri);
        assert(businessLocations.getData().get(0).getId().equals("123"));
    }
}
