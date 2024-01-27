package com.example.pifinance_back.auth;

import lombok.Data;

@Data
public class JwtLogin {

    private String email;

    private String password;
}