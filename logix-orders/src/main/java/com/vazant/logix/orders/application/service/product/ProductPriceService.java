package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.ProductPrice;
import com.vazant.logix.orders.infrastructure.repository.product.ProductPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing product prices.
 */
@Slf4j
@Service
@Transactional
public class ProductPriceService extends AbstractCrudService<ProductPrice> {

  public ProductPriceService(ProductPriceRepository productPriceRepository) {
    super(productPriceRepository, ProductPrice.class);
  }

  @Override
  protected String getEntityName() {
    return "ProductPrice";
  }
}
