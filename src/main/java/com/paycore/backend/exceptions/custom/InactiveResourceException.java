package com.paycore.backend.exceptions.custom;

public class InactiveResourceException extends RuntimeException {
    public InactiveResourceException(String message) {
        super(message);
    }
}
