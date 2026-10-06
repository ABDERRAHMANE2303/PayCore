package com.paycore.backend.dtos.responses;

public record PaymentResponse(
        PaymentDetailsResponse paymentDetailsResponse,
        TransactionDetailsResponse transactionDetails
)
{}
