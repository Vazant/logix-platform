package com.vazant.logix.orders.domain.event;

import com.vazant.logix.orders.domain.order.OrderStatus;
import java.time.LocalDateTime;

public record OrderStatusChangedEvent(
    String orderUuid, OrderStatus previousStatus, OrderStatus newStatus, LocalDateTime occurredOn) {
  public OrderStatusChangedEvent(
      String orderUuid, OrderStatus previousStatus, OrderStatus newStatus) {
    this(orderUuid, previousStatus, newStatus, LocalDateTime.now());
  }
}
