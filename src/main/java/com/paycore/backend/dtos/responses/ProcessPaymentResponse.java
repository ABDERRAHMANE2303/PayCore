package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.PaymentStatus;

import java.util.UUID;

public record ProcessPaymentResponse(
        UUID paymentId,
        PaymentStatus paymentStatus,
        TransactionResponse Transaction
        )
{}