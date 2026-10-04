package com.paycore.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerRequest(

    @NotNull
    @NotBlank
    String name
){}
