package com.example.pifinance_back.Controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String stripeToken;
    private String tokenTransaction;
    private Long id_wallet; // Ajoutez ceci

}
