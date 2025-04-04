package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.infrastructure.repository.product.CategoryRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbstractCrudService<Category> {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    super(Category.class);
    this.categoryRepository = categoryRepository;
  }

  @Override
  protected JpaRepository<Category, UUID> getRepository() {
    return categoryRepository;
  }
}
