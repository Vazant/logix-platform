package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vazant.logix.shared.Constants;

/**
 * Service for managing orders.
 * Provides only CRUD operations. Business logic is handled by OrderBusinessService.
 */
@Slf4j
@Service
@Transactional
public class OrderService extends AbstractCrudService<Order> {

  public OrderService(OrderRepository orderRepository) {
    super(orderRepository, Order.class);
  }

  @Override
  protected String getEntityName() {
    return Constants.ENTITY_ORDER;
  }
}
