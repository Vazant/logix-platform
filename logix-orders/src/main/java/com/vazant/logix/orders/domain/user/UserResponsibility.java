package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_responsibilities")
public class UserResponsibility extends BaseEntity implements Updatable<UserResponsibility> {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SystemResponsibility responsibility;

  public UserResponsibility() {}

  @JiltBuilder
  public UserResponsibility(SystemResponsibility responsibility) {
    this.responsibility = responsibility;
  }

  public SystemResponsibility getResponsibility() {
    return responsibility;
  }

  public void setResponsibility(SystemResponsibility responsibility) {
    this.responsibility = responsibility;
  }

  public String getName() {
    return responsibility.getName();
  }

  public String getAuthority() {
    return responsibility.name();
  }

  @Override
  public void doUpdate(UserResponsibility updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated UserResponsibility must not be null");
    }
    this.responsibility = updated.getResponsibility();
  }
}
