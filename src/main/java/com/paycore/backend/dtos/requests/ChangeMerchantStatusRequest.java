package com.paycore.backend.dtos.requests;

import com.paycore.backend.enums.MerchantStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeMerchantStatusRequest(
        @NotNull MerchantStatus status
){}
