package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentInfosRes(
        UUID id,
        CustomerInfosRes customer,
        MerchantInfosRes merchant,
        BigDecimal amount,
        Currency currency,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
)
{}
