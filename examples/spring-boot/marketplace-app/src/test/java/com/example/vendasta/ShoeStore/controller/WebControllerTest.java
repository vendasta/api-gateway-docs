package com.example.vendasta.ShoeStore.controller;

import com.example.vendasta.ShoeStore.ShoeStoreApplication;
import com.example.vendasta.ShoeStore.entity.business.*;
import com.example.vendasta.ShoeStore.service.ServiceAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.example.vendasta.ShoeStore.utils.Constants.PARTNERID;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { WebController.class, ShoeStoreApplication.class})
//@WebMvcTest(controllers = WebController.class)
public class WebControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ServiceAccount serviceAccount;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        Links links = new Links();
        links.setFirst("first");

        BusinessLocationsData businessLocationsData = new BusinessLocationsData();
        businessLocationsData.setId("123");
        businessLocationsData.setType("type");
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
        businessLocationsData.setAttributes(attributes);

        businessLocations.setData(new ArrayList<>(List.of(businessLocationsData)));


        Mockito.when(serviceAccount.fetchBusinessLocations(Mockito.anyString())).thenReturn(businessLocations);
        //Mockito.when(restTemplate.exchange(uri, HttpMethod.GET, request, BusinessLocations.class)).thenReturn(myEntity);

    }

    @Test
    public void home() throws Exception {
        mockMvc.perform(get("/AG-1234567890")
                        .with(oidcLogin()))
                .andExpect(status().isOk());
    }

    @Test
    public void entry() throws Exception {
        mockMvc.perform(get("/entry/AG-1234567890")
                        .with(oidcLogin()))
                //"Moved temporarily" is deprecated
                .andExpect(status().isFound());
    }

    @Test
    public void testBusinessLocations() throws Exception {

        mockMvc.perform(get("/business/list/1234")
                        .with(oidcLogin()))
                .andExpect(status().isOk());
    }
}