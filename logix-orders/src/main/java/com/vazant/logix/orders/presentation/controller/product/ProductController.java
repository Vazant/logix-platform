package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing products.
 * <p>
 * Provides CRUD operations and product-specific endpoints like search by category.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController<Product> {

  public ProductController(CrudService<Product> productService) {
    super(productService);
  }

  /**
   * Find products by category.
   *
   * @param categoryId the category UUID
   * @return list of products in the category
   */
  @GetMapping("/category/{categoryId}")
  public ResponseEntity<List<Product>> findByCategory(@PathVariable @ValidUuid String categoryId) {
    // TODO: Implement product search by category
    // This would require adding a method to ProductService or creating a ProductBusinessService
    return ResponseEntity.ok(List.of());
  }

  /**
   * Search products by name.
   *
   * @param name the product name to search for
   * @return list of matching products
   */
  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
    // TODO: Implement product search by name
    return ResponseEntity.ok(List.of());
  }

  /**
   * Find products by price range.
   *
   * @param minPrice minimum price
   * @param maxPrice maximum price
   * @return list of products in the price range
   */
  @GetMapping("/price-range")
  public ResponseEntity<List<Product>> findByPriceRange(
      @RequestParam Double minPrice, @RequestParam Double maxPrice) {
    // TODO: Implement product search by price range
    return ResponseEntity.ok(List.of());
  }
}
