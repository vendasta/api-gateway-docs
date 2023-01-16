package com.example.vendasta.ShoeStore.utils;


public class Constants {
    public static final String AUTH_CALLBACK_URL = "/login/oauth2/code/apigateway/";
    public static final String CLIENT_REGISTRATION_ID = "apigateway";
    // Partner id and the subdomain (prod/demo) should be added dynamically in below URL.
    public static final String BUSINESS_LOCATIONS_API_URL = "https://%s.apigateway.co/platform/businessLocations?filter[businessPartner.id]=%s";
    public static final String GET_BUSINESS_LOCATION = "https://%s.apigateway.co/platform/businessLocations/%s";
}
