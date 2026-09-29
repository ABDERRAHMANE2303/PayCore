package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.MerchantCategory;
import com.paycore.backend.enums.MerchantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record MerchantInfosRes(
        UUID id,
        String name,
        MerchantCategory category,
        MerchantStatus status,
        LocalDateTime createdAt
) { }
