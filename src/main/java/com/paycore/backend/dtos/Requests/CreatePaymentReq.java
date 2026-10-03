package com.paycore.backend.dtos.Requests;

import com.paycore.backend.enums.Currency;
import com.paycore.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentReq(

        @NotNull UUID customerID,
        @NotNull UUID merchantID,
        @NotNull @Positive BigDecimal amount,
        @NotNull Currency currency,
        @NotNull PaymentMethod method
)
{}
