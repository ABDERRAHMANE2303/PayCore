package com.paycore.backend.exceptions.custom;

public class InvalidSortOptionException extends RuntimeException {
    public InvalidSortOptionException(String message) {
        super(message);
    }
}
