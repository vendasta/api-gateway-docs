package com.example.vendasta.ShoeStore.entity.business;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String stateCode;
    private String zip;
    private String line1;
    private String line2;
    private String city;
    private String postalCode;
    private String countryCode;
    private String regionCode;
}