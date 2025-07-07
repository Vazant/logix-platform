package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a product category.
 * <p>
 * Stores the name and description of a category and supports updating from another instance.
 */
@Entity
@Table(name = Constants.ENTITY_CATEGORY)
public class Category extends BaseEntity implements Updatable<Category> {

  @NotBlank(message = "Name must not be blank")
  private String name;

  @Size(max = 1000, message = "Description must be at most 1000 characters")
  private String description;

  /**
   * Default constructor for JPA.
   */
  protected Category() {}

  /**
   * Constructs a new Category with the specified name and description.
   *
   * @param name the category name
   * @param description the category description
   */
  @JiltBuilder
  public Category(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the category name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the category description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the category name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the category description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Updates this category from another instance.
   *
   * @param updated the updated category
   */
  @Override
  public void doUpdate(Category updated) {
    this.name = updated.getName();
    this.description = updated.getDescription();
  }
}
