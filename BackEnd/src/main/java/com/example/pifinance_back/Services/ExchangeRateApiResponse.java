package com.example.pifinance_back.Services;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
class ExchangeRateApiResponse {
    private Map<String, Double> conversion_rates;
}
