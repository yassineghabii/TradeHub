// ReclamationNotFoundException.java
package com.example.pifinance_back.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReclamationNotFoundException extends RuntimeException {

    public ReclamationNotFoundException(String message) {
        super(message);
    }
}
