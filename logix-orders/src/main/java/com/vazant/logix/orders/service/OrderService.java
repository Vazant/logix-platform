package com.vazant.logix.orders.service;

import com.vazant.logix.orders.domain.OrderBuilder;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.dto.OrderRequest;
import com.vazant.logix.orders.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final OrderRepository repository;

  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public Order createOrder(OrderRequest request) {
    validateRequest(request);

    Order order =
        OrderBuilder.order()
            .customerId(request.customerId())
            .warehouseId(request.warehouseId())
            .amount(request.amount())
            .description(request.description())
            .build();

    Order saved = repository.save(order);
    log.info("✅ Order created: {}", saved.getUuid());

    return saved;
  }

  private void validateRequest(OrderRequest request) {
    if (!StringUtils.hasText(request.customerId())) {
      throw new IllegalArgumentException("Customer ID must not be empty");
    }
    if (!StringUtils.hasText(request.warehouseId())) {
      throw new IllegalArgumentException("Warehouse ID must not be empty");
    }
    if (request.amount() == null || request.amount().doubleValue() <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
  }
}
