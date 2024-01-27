package com.example.pifinance_back.Services;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Aucun utilisateur trouvé avec l'ID: " + id);
    }

}
