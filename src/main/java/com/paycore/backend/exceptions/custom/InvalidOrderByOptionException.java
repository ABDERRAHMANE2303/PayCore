package com.paycore.backend.exceptions.custom;

public class InvalidOrderByOptionException extends RuntimeException {
    public InvalidOrderByOptionException(String message) {
        super(message);
    }
}
