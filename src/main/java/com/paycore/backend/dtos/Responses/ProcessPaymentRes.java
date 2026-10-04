package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.PaymentStatus;

import java.util.UUID;

public record ProcessPaymentRes(
        UUID paymentId,
        PaymentStatus paymentStatus,
        TransactionRes processTransaction
        )
{}