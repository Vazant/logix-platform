package com.vazant.logix.orders.domain.common;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base class for all JPA entities in the system.
 * <p>
 * Provides UUID as primary key and auditing fields for creation and update timestamps.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID uuid;

  @CreatedDate
  @Column(
      nullable = false,
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updatedAt;

  /**
   * Returns the unique identifier (UUID) of the entity.
   *
   * @return the UUID
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Returns the creation timestamp of the entity.
   *
   * @return the creation time
   */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns the last update timestamp of the entity.
   *
   * @return the last update time
   */
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Sets the last update timestamp of the entity.
   *
   * @param updatedAt the new update time
   */
  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
