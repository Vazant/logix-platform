package com.vazant.logix.orders.infrastructure.repository.product;

import com.vazant.logix.orders.domain.product.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {}
