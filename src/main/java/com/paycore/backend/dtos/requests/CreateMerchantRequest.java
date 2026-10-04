package com.paycore.backend.dtos.requests;

import com.paycore.backend.enums.MerchantCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMerchantRequest(
        @NotNull @NotBlank  String name,
        @NotNull MerchantCategory category
) {}
