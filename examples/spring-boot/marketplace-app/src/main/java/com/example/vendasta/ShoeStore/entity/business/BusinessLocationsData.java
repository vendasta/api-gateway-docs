package com.example.vendasta.ShoeStore.entity.business;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessLocationsData {
    private String type;
    private String id;
    private Attributes attributes;
    private Relationships relationships;
}