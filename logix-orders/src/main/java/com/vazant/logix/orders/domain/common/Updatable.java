package com.vazant.logix.orders.domain.common;

/**
 * Interface for entities that support updating their state from another instance.
 *
 * @param <T> the entity type
 */
public interface Updatable<T> {
  /**
   * Updates this entity from another instance.
   *
   * @param updated the updated instance
   * @throws IllegalArgumentException if updated is null
   */
  default void updateFrom(T updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated object must not be null");
    }
    doUpdate(updated);
  }

  /**
   * Performs the actual update logic for the entity.
   *
   * @param updated the updated instance
   */
  void doUpdate(T updated);
}
