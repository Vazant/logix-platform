package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractCrudService<Product> {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    super(Product.class);
    this.productRepository = productRepository;
  }

  @Override
  protected JpaRepository<Product, UUID> getRepository() {
    return productRepository;
  }
}
