package com.vazant.logix.orders.dto;

import java.math.BigDecimal;

public record OrderRequest(
    String customerId, String warehouseId, BigDecimal amount, String description) {}
