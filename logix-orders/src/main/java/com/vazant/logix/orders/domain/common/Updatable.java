package com.vazant.logix.orders.domain.common;

public interface Updatable<T> {
  default void updateFrom(T updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated object must not be null");
    }
    doUpdate(updated);
  }

  void doUpdate(T updated);
}
