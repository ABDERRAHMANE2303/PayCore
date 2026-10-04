package com.paycore.backend.dtos.responses;

public record ApiErrorResponse(
        int status,
        String message
) {}
