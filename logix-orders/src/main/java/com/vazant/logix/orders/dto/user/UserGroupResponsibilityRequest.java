package com.vazant.logix.orders.dto.user;

import com.vazant.logix.orders.domain.user.UserResponsibility;

/**
 * Request DTO for assigning a responsibility to a user group.
 * <p>
 * Contains a user responsibility to be assigned to a group.
 */
public class UserGroupResponsibilityRequest {
  /**
   * The user responsibility to assign to the group.
   */
  private UserResponsibility responsibility;

  /**
   * Returns the user responsibility to assign.
   *
   * @return the user responsibility
   */
  public UserResponsibility getResponsibility() {
    return responsibility;
  }

  /**
   * Sets the user responsibility to assign.
   *
   * @param responsibility the user responsibility
   */
  public void setResponsibility(UserResponsibility responsibility) {
    this.responsibility = responsibility;
  }
}
