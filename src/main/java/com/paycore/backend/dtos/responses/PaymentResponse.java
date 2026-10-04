package com.paycore.backend.dtos.responses;

public record PaymentResponse(
        PaymentDetails paymentDetails,
        TransactionDetails transactionDetails
)
{}
