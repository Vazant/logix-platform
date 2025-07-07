package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing product categories.
 * <p>
 * Provides CRUD operations for product categories.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController extends BaseController<Category> {

  public CategoryController(CrudService<Category> categoryService) {
    super(categoryService);
  }

  /**
   * Find categories by name containing the search term.
   *
   * @param name the name to search for
   * @return list of matching categories
   */
  @GetMapping("/search")
  public ResponseEntity<List<Category>> searchByName(@RequestParam String name) {
    // TODO: Implement category search by name
    // This would require adding a method to CategoryService
    return ResponseEntity.ok(List.of());
  }
}
