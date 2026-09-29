package com.paycore.backend.dtos.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerReq(

    @NotNull
    @NotBlank
    String name
){}
