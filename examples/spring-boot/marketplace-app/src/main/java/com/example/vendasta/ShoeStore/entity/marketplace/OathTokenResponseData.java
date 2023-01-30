package com.example.vendasta.ShoeStore.entity.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OathTokenResponseData {
    public String access_token;
    public String token_type;
    public int expires;
}
