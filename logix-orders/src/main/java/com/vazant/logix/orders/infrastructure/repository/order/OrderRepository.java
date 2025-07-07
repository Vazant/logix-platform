package com.vazant.logix.orders.infrastructure.repository.order;

import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {
  
  @Query("SELECT o FROM Order o WHERE o.customer.uuid = :customerUuid")
  List<Order> findByCustomerUuid(@Param("customerUuid") UUID customerUuid);
  
  @Query("SELECT o FROM Order o WHERE o.status = :status")
  List<Order> findByStatus(@Param("status") OrderStatus status);
  
  @Query("SELECT o FROM Order o WHERE o.organization.uuid = :organizationUuid")
  List<Order> findByOrganizationUuid(@Param("organizationUuid") UUID organizationUuid);
}
