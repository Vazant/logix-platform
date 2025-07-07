package com.vazant.logix.orders.dto.order;

import com.vazant.logix.orders.dto.shared.MoneyRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO representing a single item in an order.
 * <p>
 * Contains the product identifier, quantity, and price for the item.
 * Used for order creation and update operations.
 *
 * @param productUuid the unique identifier of the product
 * @param quantity the quantity of the product (must be at least 1)
 * @param price the price of the item
 */
public record OrderItemRequest(
    @NotBlank String productUuid, @Min(1) int quantity, @NotNull MoneyRequest price) {}
