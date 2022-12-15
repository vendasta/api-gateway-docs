package com.example.vendasta.ShoeStore.entity.business;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attributes {
    private String customerIdentifier;
    private String name;
    private Address address;
    private GeoCoordinate geoCoordinate;
    private Boolean serviceAreaBusiness;
    private List<String> phoneNumbers = new ArrayList<String>();
    private Object hours;
}