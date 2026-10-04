package com.paycore.backend.dtos.Responses;

import com.paycore.backend.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRes(
        UUID transactionId,
        TransactionStatus transactionStatus,
        BigDecimal fee
)
{}
