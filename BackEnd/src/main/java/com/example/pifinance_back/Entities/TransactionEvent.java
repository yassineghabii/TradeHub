package com.example.pifinance_back.Entities;
import org.springframework.context.ApplicationEvent;

public class TransactionEvent extends ApplicationEvent {

    private Transaction transaction;

    public TransactionEvent(Object source, Transaction transaction) {
        super(source);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
