package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        LocalDateTime createdAt,
        CustomerStatus status
){}
