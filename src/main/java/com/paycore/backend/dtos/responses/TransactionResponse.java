package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        TransactionStatus transactionStatus,
        BigDecimal fee
)
{}
