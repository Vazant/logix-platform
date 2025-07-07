package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Entity representing a user responsibility (role) in the system.
 * <p>
 * Associates a user with a specific system responsibility for access control.
 */
@Entity
@Table(name = "user_responsibilities")
public class UserResponsibility extends BaseEntity implements Updatable<UserResponsibility> {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SystemResponsibility responsibility;

  /**
   * Default constructor for JPA.
   */
  public UserResponsibility() {}

  /**
   * Constructs a new UserResponsibility with the specified system responsibility.
   *
   * @param responsibility the system responsibility
   */
  @JiltBuilder
  public UserResponsibility(SystemResponsibility responsibility) {
    this.responsibility = responsibility;
  }

  /**
   * Returns the system responsibility associated with this user responsibility.
   *
   * @return the system responsibility
   */
  public SystemResponsibility getResponsibility() {
    return responsibility;
  }

  /**
   * Sets the system responsibility for this user responsibility.
   *
   * @param responsibility the system responsibility
   */
  public void setResponsibility(SystemResponsibility responsibility) {
    this.responsibility = responsibility;
  }

  /**
   * Returns the name of the responsibility (for authority mapping).
   *
   * @return the responsibility name
   */
  public String getName() {
    return responsibility.getName();
  }

  /**
   * Returns the authority string for this responsibility (enum name).
   *
   * @return the authority string
   */
  public String getAuthority() {
    return responsibility.name();
  }

  /**
   * Updates this user responsibility from another instance.
   *
   * @param updated the updated user responsibility
   * @throws IllegalArgumentException if updated is null
   */
  @Override
  public void doUpdate(UserResponsibility updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated UserResponsibility must not be null");
    }
    this.responsibility = updated.getResponsibility();
  }
}
