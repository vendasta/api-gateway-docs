package com.example.vendasta.ShoeStore.entity.business;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessCategories {
    private BusinessCategoryLinks links;
    private List<BusinessCategoryData> data = new ArrayList<BusinessCategoryData>();
}