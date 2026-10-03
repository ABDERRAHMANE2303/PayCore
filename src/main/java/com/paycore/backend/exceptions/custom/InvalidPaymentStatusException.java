package com.paycore.backend.exceptions.custom;

public class InvalidPaymentStatusException extends RuntimeException {
    public InvalidPaymentStatusException(String message) {
        super(message);
    }
}
