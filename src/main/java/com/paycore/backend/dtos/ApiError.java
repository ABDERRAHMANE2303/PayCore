package com.paycore.backend.dtos;

public record ApiError(
        int status,
        String message
) {}
