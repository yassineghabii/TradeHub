package com.example.pifinance_back.Entities;

public class QuantiteInsuffisanteException extends RuntimeException {
    public QuantiteInsuffisanteException(String s) {
        super("Quantité insuffisante pour l'achat.");
    }
}
