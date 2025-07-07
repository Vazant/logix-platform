package com.vazant.logix.orders.presentation.controller.product;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.product.ProductPrice;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing product prices.
 * <p>
 * Provides CRUD operations for product prices.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/product-prices")
public class ProductPriceController extends BaseController<ProductPrice> {

  public ProductPriceController(CrudService<ProductPrice> productPriceService) {
    super(productPriceService);
  }

  /**
   * Find product prices by product UUID.
   *
   * @param productId the product UUID
   * @return list of prices for the product
   */
  @GetMapping("/product/{productId}")
  public ResponseEntity<List<ProductPrice>> findByProduct(@PathVariable String productId) {
    // TODO: Implement product price search by product
    // This would require adding a method to ProductPriceService
    return ResponseEntity.ok(List.of());
  }

  /**
   * Find product prices by currency.
   *
   * @param currency the currency code
   * @return list of prices in the specified currency
   */
  @GetMapping("/currency/{currency}")
  public ResponseEntity<List<ProductPrice>> findByCurrency(@PathVariable String currency) {
    // TODO: Implement product price search by currency
    return ResponseEntity.ok(List.of());
  }
}
