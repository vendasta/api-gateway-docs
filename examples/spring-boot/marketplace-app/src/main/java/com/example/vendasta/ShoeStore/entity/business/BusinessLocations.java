package com.example.vendasta.ShoeStore.entity.business;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessLocations {
    private Links links;
    private List<BusinessLocation> data = new ArrayList<BusinessLocation>();
}