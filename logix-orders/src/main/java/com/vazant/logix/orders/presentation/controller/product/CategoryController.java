package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.product.CategoryService;
import com.vazant.logix.orders.domain.product.Category;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> category = categoryService.findAll();
    return ResponseEntity.ok(category);
  }

  @GetMapping("/{categoryUuid}")
  public ResponseEntity<Category> getCategory(@PathVariable String categoryUuid) {
    Category product = categoryService.findByUuid(categoryUuid);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody Category product) {
    Category created = categoryService.create(product);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{categoryUuid}")
  public ResponseEntity<Category> updateCategory(
      @PathVariable String categoryUuid, @RequestBody Category category) {
    Category updated = categoryService.update(categoryUuid, category);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{categoryUuid}")
  public ResponseEntity<Void> deleteCategory(@PathVariable String categoryUuid) {
    categoryService.delete(categoryUuid);
    return ResponseEntity.noContent().build();
  }
}
