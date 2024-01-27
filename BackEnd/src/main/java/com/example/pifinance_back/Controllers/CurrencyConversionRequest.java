package com.example.pifinance_back.Controllers;

import java.math.BigDecimal;

public class CurrencyConversionRequest {
    private BigDecimal amount;
    private String fromCurrencyCode;
    private String toCurrencyCode;

    // Getters et Setters
    // ...

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }
}
