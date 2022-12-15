package com.example.vendasta.ShoeStore.entity.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssertionPayloadData {
    private String aud;
    private String iss;
    private String sub;
}

