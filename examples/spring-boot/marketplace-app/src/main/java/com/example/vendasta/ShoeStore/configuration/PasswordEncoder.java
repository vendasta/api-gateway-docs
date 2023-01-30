package com.example.vendasta.ShoeStore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

@Configuration
public class PasswordEncoder {
    @Bean
    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
