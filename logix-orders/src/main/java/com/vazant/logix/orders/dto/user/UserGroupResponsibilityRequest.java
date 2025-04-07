package com.vazant.logix.orders.dto.user;

import com.vazant.logix.orders.domain.user.UserResponsibility;

public class UserGroupResponsibilityRequest {
  private UserResponsibility responsibility;

  public UserResponsibility getResponsibility() {
    return responsibility;
  }

  public void setResponsibility(UserResponsibility responsibility) {
    this.responsibility = responsibility;
  }
}
