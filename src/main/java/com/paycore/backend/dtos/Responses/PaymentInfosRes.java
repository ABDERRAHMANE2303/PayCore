package com.paycore.backend.dtos.Responses;

import com.paycore.backend.entities.Customer;
import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import com.paycore.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentInfosRes(
        UUID id,
        CustomerInfosRes customer,
        MerchantInfosRes merchant,
        BigDecimal amount,
        Currency currnecy,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt
)
{}
