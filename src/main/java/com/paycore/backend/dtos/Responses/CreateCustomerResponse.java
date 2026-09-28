package com.paycore.backend.dtos.Responses;

import com.paycore.backend.entities.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCustomerResponse (
        UUID id,
        String name,
        LocalDateTime createdAt,
        Customer.CustomerStatus status
){}
