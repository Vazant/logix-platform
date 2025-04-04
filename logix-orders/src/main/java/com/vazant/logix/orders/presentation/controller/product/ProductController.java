package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.product.ProductService;
import com.vazant.logix.orders.domain.product.Product;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{productUuid}")
  public ResponseEntity<Product> getProduct(@PathVariable String productUuid) {
    Product product = productService.findByUuid(productUuid);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product created = productService.create(product);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{productUuid}")
  public ResponseEntity<Product> updateProduct(
      @PathVariable String productUuid, @RequestBody Product product) {
    Product updated = productService.update(productUuid, product);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{productUuid}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String productUuid) {
    productService.delete(productUuid);
    return ResponseEntity.noContent().build();
  }
}
