package com.vazant.logix.orders.repository;

import com.vazant.logix.orders.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
