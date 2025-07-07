package com.vazant.logix.orders.application.service.product;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for managing products.
 */
@Slf4j
@Service
@Transactional
public class ProductService extends AbstractCrudService<Product> {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    super(productRepository, Product.class);
    this.productRepository = productRepository;
  }

  @Override
  protected String getEntityName() {
    return "Product";
  }

  /**
   * Finds products by category.
   *
   * @param categoryId the category ID
   * @return list of products
   */
  public List<Product> findByCategory(String categoryId) {
    Assert.hasText(categoryId, "Category ID must not be null or empty");
    return productRepository.findByCategoryUuid(UuidUtils.parse(categoryId));
  }

  /**
   * Finds products by organization.
   *
   * @param organizationId the organization ID
   * @return list of products
   */
  public List<Product> findByOrganization(String organizationId) {
    Assert.hasText(organizationId, "Organization ID must not be null or empty");
    return productRepository.findByOrganizationUuid(UuidUtils.parse(organizationId));
  }
}
