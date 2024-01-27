package com.example.pifinance_back.Entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlphaVantageConfig {


    @Value("${alphavantage.api.key}")
    private String alphaVantageApiKey;

    public String getAlphaVantageApiKey() {
        return alphaVantageApiKey;
    }
}
