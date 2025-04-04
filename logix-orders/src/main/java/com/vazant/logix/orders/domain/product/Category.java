package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity implements Updatable<Category> {

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

  @Override
  public void doUpdate(Category updated) {
    this.name = updated.getName();
    this.description = updated.getDescription();
  }
}
