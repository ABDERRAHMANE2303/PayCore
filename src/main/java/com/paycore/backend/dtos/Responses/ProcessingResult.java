package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessingResult(
        UUID paymentId,
        PaymentStatus status,
        BigDecimal fee
)
{}
