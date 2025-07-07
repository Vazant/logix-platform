package com.vazant.logix.orders.domain.order;

/**
 * Enumeration of possible order statuses.
 * <p>
 * Provides logic for valid status transitions.
 */
public enum OrderStatus {
  CREATED,
  PENDING,
  CONFIRMED,
  PROCESSING,
  PAID,
  IN_PROGRESS,
  SHIPPED,
  DELIVERED,
  CANCELLED;

  /**
   * Determines if this status can transition to the target status.
   *
   * @param targetStatus the target status
   * @return true if transition is allowed, false otherwise
   */
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
