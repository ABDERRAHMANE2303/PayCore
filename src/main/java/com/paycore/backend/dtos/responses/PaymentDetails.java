package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDetails(
        UUID id,
        CustomerDetailsResponse customer,
        MerchantDetailResponse merchant,
        BigDecimal amount,
        Currency currency,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
){}
