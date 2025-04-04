package com.vazant.logix.orders.dto.order;

import com.vazant.logix.orders.domain.shared.Money;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotBlank String productId, @Min(1) int quantity, @NotNull Money price) {}
