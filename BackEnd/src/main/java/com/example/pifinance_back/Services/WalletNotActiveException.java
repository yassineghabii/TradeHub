package com.example.pifinance_back.Services;

public class WalletNotActiveException extends RuntimeException {

    public WalletNotActiveException() {
        super();
    }

    public WalletNotActiveException(String message) {
        super(message);
    }

    public WalletNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletNotActiveException(Throwable cause) {
        super(cause);
    }
}
