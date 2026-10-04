package com.paycore.backend.processors;

import com.paycore.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessingResult(
        UUID paymentId,
        PaymentStatus status,
        BigDecimal fee
)
{}
