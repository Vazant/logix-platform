package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.ProductPrice;
import com.vazant.logix.orders.infrastructure.repository.product.ProductPriceRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceService extends AbstractCrudService<ProductPrice> {
  private final ProductPriceRepository productPriceRepository;

  public ProductPriceService(ProductPriceRepository productPriceRepository) {
    super(ProductPrice.class);
    this.productPriceRepository = productPriceRepository;
  }

  @Override
  protected JpaRepository<ProductPrice, UUID> getRepository() {
    return productPriceRepository;
  }
}
