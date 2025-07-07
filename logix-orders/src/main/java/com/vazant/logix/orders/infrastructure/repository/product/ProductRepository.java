package com.vazant.logix.orders.infrastructure.repository.product;

import com.vazant.logix.orders.domain.product.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  
  @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryUuid")
  List<Product> findByCategoryUuid(@Param("categoryUuid") UUID categoryUuid);
  
  @Query("SELECT p FROM Product p WHERE p.organization.uuid = :organizationUuid")
  List<Product> findByOrganizationUuid(@Param("organizationUuid") UUID organizationUuid);
}
