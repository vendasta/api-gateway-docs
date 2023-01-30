package com.example.vendasta.ShoeStore.entity.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccountJson {
    private String type;
    private AssertionHeaderData assertionHeaderData;
    private AssertionPayloadData assertionPayloadData;
    @JsonProperty("private_key_id")
    private String privateKeyId;
    @JsonProperty("private_key")
    private String privateKey;
    @JsonProperty("client_email")
    private String clientEmail;
    @JsonProperty("token_uri")
    private String tokenUri;
}

