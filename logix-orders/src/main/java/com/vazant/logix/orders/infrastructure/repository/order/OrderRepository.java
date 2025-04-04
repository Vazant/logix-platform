package com.vazant.logix.orders.infrastructure.repository.order;

import com.vazant.logix.orders.domain.order.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {}
