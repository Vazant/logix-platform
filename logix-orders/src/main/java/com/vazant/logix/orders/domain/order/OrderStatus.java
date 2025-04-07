package com.vazant.logix.orders.domain.order;

public enum OrderStatus {
  CREATED,
  PAID,
  IN_PROGRESS,
  SHIPPED,
  DELIVERED,
  CANCELLED;

  public boolean canTransitionTo(OrderStatus targetStatus) {
    if (this == DELIVERED || this == CANCELLED) {
      return false;
    }
    return switch (this) {
      case CREATED -> targetStatus == PAID || targetStatus == CANCELLED;
      case PAID -> targetStatus == IN_PROGRESS || targetStatus == CANCELLED;
      case IN_PROGRESS -> targetStatus == SHIPPED || targetStatus == CANCELLED;
      case SHIPPED -> targetStatus == DELIVERED;
      default -> false;
    };
  }
}
