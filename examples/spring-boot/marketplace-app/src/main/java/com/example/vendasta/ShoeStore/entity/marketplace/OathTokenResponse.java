package com.example.vendasta.ShoeStore.entity.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OathTokenResponse {
    public int took;
    public OathTokenResponseData data;
}
