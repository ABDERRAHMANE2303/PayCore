package com.paycore.backend.dtos.responses;

import com.paycore.backend.enums.TransactionStatus;
import com.paycore.backend.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDetails(
        UUID id,
        TransactionType transactonType,
        TransactionStatus transactionStatus,
        BigDecimal amount,
        BigDecimal fee,
        LocalDateTime createdAt
)
{}
