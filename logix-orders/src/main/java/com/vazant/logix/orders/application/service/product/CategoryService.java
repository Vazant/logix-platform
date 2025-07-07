package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.infrastructure.repository.product.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing product categories.
 * <p>
 * Provides CRUD operations and business logic for product categories.
 */
@Slf4j
@Service
@Transactional
public class CategoryService extends AbstractCrudService<Category> {

  public CategoryService(CategoryRepository categoryRepository) {
    super(categoryRepository, Category.class);
  }

  /**
   * Returns the entity name for logging and error messages.
   *
   * @return the entity name
   */
  @Override
  protected String getEntityName() {
    return "Category";
  }
}
