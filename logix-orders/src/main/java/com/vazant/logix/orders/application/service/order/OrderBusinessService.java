package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderStatus;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.dto.order.OrderRequest;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business service for order operations.
 * Contains business logic for orders, delegating CRUD operations to CrudService.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderBusinessService {

  private final CrudService<Order> orderCrudService;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderCreationService orderCreationService;

  /**
   * Creates a new order from request.
   *
   * @param request the order request
   * @param organization the organization
   * @return the created order
   */
  public Order createOrder(OrderRequest request, Organization organization) {
    return orderCrudService.create(orderCreationService.createOrder(request, organization));
  }

  /**
   * Updates order status.
   *
   * @param orderId the order ID
   * @param status the new status
   * @return the updated order
   */
  public Order updateStatus(String orderId, OrderStatus status) {
    Order order = orderCrudService.findByUuid(orderId);
    order.setStatus(status);
    order.setUpdatedAt(LocalDateTime.now());
    return orderCrudService.update(orderId, order);
  }

  /**
   * Finds orders by customer.
   *
   * @param customerId the customer ID
   * @return list of orders
   */
  public List<Order> findByCustomer(String customerId) {
    return orderRepository.findByCustomerUuid(UuidUtils.parse(customerId));
  }

  /**
   * Finds orders by status.
   *
   * @param status the order status
   * @return list of orders
   */
  public List<Order> findByStatus(OrderStatus status) {
    return orderRepository.findByStatus(status);
  }

  /**
   * Finds orders by organization.
   *
   * @param organizationId the organization ID
   * @return list of orders
   */
  public List<Order> findByOrganization(String organizationId) {
    return orderRepository.findByOrganizationUuid(UuidUtils.parse(organizationId));
  }

  /**
   * Validates that customer exists.
   *
   * @param customerId the customer ID
   * @return the customer
   * @throws IllegalArgumentException if customer not found
   */
  public Customer validateCustomer(String customerId) {
    return customerRepository.findById(UuidUtils.parse(customerId))
        .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
  }

  /**
   * Validates that product exists.
   *
   * @param productId the product ID
   * @return the product
   * @throws IllegalArgumentException if product not found
   */
  public Product validateProduct(String productId) {
    return productRepository.findById(UuidUtils.parse(productId))
        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
  }
} 