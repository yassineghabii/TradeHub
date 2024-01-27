package com.example.pifinance_back.Controllers;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class CardDetailsDTO {
    private String number;
    private int expMonth;
    private int expYear;
    private String cvc;
    private BigDecimal amount;
    private Currency currency;
}