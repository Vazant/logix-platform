package com.vazant.logix.orders.infrastructure.repository.product;

import com.vazant.logix.orders.domain.product.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {}
