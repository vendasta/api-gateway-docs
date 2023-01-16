package com.example.vendasta.ShoeStore.entity.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketPlaceOathTokenRequest {

    public String grant_type;
    public String assertion;
}
