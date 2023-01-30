package com.example.vendasta.ShoeStore.entity.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssertionHeaderData {
    private String alg;
    private String kid;
}
