package com.paycore.backend.dtos.Requests;

import com.paycore.backend.enums.MerchantStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeMerchantStatusReq(
        @NotNull MerchantStatus status
){}
