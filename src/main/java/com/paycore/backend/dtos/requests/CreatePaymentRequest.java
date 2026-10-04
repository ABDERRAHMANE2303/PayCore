package com.paycore.backend.dtos.requests;

import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull UUID customerId,
        @NotNull UUID merchantId,
        @NotNull @Positive BigDecimal amount,
        @NotNull Currency currency,
        @NotNull PaymentMethod method
)
{}
