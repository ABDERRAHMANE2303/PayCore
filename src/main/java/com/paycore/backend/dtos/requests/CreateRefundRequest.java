package com.paycore.backend.dtos.requests;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateRefundRequest(
        @NotNull @Positive BigDecimal amount
)
{}
