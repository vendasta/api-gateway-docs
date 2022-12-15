package com.example.vendasta.ShoeStore.utils;


public class Constants {
    public static final String APPID = "< APP-ID >";
    public static final String PARTNERID = "< PARTNER-ID >";
    public static final String SERVICE_ACCOUNT_JSON_PATH = "< SERVICE-ACCOUNT-JSON-PATH >";
    public static final String AUTH_CALLBACK_URL = "/callback";
    public static final String CLIENT_REGISTRATION_ID = "vendasta";
    // Partner id and the subdomain (prod/demo) should be added dynamically in below URL.
    public static final String BUSINESS_LOCATIONS_API_URL = "https://%s.apigateway.co/platform/businessLocations?filter[businessPartner.id]=%s";
}
