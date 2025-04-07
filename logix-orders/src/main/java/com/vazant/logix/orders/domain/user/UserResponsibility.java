package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "user_responsibilities")
public class UserResponsibility extends BaseEntity implements Updatable<UserResponsibility> {

  @NotBlank private String name; // Например, "MANAGE_ORDERS", "VIEW_REPORTS" и т.д.

  private String description;

  public UserResponsibility() {}

  @JiltBuilder
  public UserResponsibility(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public void doUpdate(UserResponsibility updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated UserResponsibility must not be null");
    }
    this.name = updated.getName();
    this.description = updated.getDescription();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
