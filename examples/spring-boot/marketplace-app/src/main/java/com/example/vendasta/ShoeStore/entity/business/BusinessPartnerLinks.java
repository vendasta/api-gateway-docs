package com.example.vendasta.ShoeStore.entity.business;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPartnerLinks {
    private String related;
    private String self;
}