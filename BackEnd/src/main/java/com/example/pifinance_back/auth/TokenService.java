package com.example.pifinance_back.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Getter
@Setter@AllArgsConstructor @Service
public class TokenService {

    private static final SecureRandom random = new SecureRandom();

    public String generateSixDigitToken() {
        int token = 100000 + random.nextInt(900000); // This gives a range from 100000 to 999999
        return String.valueOf(token);
    }
}
