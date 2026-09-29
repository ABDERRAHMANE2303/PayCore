package com.paycore.backend.dtos.Requests;

import com.paycore.backend.enums.MerchantCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMerchantReq(
        @NotNull @NotBlank  String name,
        @NotNull MerchantCategory category
) {}
