package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.common.BaseEntity;
import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

  @NotBlank(message = "Name must not be blank")
  private String name;

  @Size(max = 1000, message = "Description must be at most 1000 characters")
  private String description;

  protected Category() {}

  @JiltBuilder
  public Category(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
