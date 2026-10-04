package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        MerchantCategory category,
        MerchantStatus status,
        LocalDateTime createdAt
) { }
