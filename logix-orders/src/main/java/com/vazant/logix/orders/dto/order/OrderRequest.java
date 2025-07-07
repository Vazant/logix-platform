package com.vazant.logix.orders.dto.order;

import com.vazant.logix.orders.dto.shared.MoneyRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO representing an order.
 * <p>
 * Contains customer and warehouse identifiers, total price, description, and a list of order items.
 * Used for order creation and update operations.
 *
 * @param customerUuid the unique identifier of the customer
 * @param warehouseUuid the unique identifier of the warehouse
 * @param total the total price of the order
 * @param description an optional description of the order
 * @param items the list of order items
 */
public record OrderRequest(
    @NotBlank String customerUuid,
    @NotBlank String warehouseUuid,
    @NotNull MoneyRequest total,
    String description,
    @NotNull @Valid List<OrderItemRequest> items) {}
