package com.example.pifinance_back.Controllers;

public class ReponseOrdre {
    private String message;

    public ReponseOrdre(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
