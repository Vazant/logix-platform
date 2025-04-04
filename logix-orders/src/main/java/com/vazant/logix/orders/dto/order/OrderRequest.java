package com.vazant.logix.orders.dto.order;

import com.vazant.logix.orders.domain.shared.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
    @NotBlank String customerUuid,
    @NotBlank String warehouseUuid,
    @NotNull Money total,
    String description,
    @NotNull @Valid List<OrderItemRequest> items) {}
