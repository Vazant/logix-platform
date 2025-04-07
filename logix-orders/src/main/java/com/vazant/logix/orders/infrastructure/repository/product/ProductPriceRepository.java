package com.vazant.logix.orders.infrastructure.repository.product;

import com.vazant.logix.orders.domain.product.ProductPrice;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {}
