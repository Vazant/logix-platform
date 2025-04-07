package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.product.ProductPriceService;
import com.vazant.logix.orders.domain.product.ProductPrice;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productPrices")
public class ProductPriceController {

  private final ProductPriceService productPriceService;

  public ProductPriceController(ProductPriceService productPriceService) {
    this.productPriceService = productPriceService;
  }

  @GetMapping
  public ResponseEntity<List<ProductPrice>> getAllProductPrices() {
    List<ProductPrice> productPrices = productPriceService.findAll();
    return ResponseEntity.ok(productPrices);
  }

  @GetMapping("/{productPriceUuid}")
  public ResponseEntity<ProductPrice> getProductPrice(@PathVariable String productPriceUuid) {
    ProductPrice product = productPriceService.findByUuid(productPriceUuid);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<ProductPrice> createProductPrice(@RequestBody ProductPrice product) {
    ProductPrice created = productPriceService.create(product);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{productPriceUuid}")
  public ResponseEntity<ProductPrice> updateProductPrice(
      @PathVariable String productPriceUuid, @RequestBody ProductPrice productPrice) {
    ProductPrice updated = productPriceService.update(productPriceUuid, productPrice);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{productPriceUuid}")
  public ResponseEntity<Void> deleteProductPrice(@PathVariable String productPriceUuid) {
    productPriceService.delete(productPriceUuid);
    return ResponseEntity.noContent().build();
  }
}
