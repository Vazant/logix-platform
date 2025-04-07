package com.vazant.logix.orders.dto.order;

import com.vazant.logix.orders.dto.shared.MoneyRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotBlank String productUuid, @Min(1) int quantity, @NotNull MoneyRequest price) {}
