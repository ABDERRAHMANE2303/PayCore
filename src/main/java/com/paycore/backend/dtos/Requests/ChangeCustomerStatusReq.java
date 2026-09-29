package com.paycore.backend.dtos.Requests;

import com.paycore.backend.enums.CustomerStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeCustomerStatusReq(
        @NotNull CustomerStatus status
){ }
