package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerInfosRes(
        UUID id,
        String name,
        LocalDateTime createdAt,
        CustomerStatus status
){}
