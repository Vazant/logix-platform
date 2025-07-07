package com.vazant.logix.orders.presentation.controller.order;

import com.vazant.logix.orders.application.service.order.OrderBusinessService;
import com.vazant.logix.orders.application.service.order.OrderService;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderStatus;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.dto.order.OrderRequest;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing orders.
 * <p>
 * Provides CRUD operations and order-specific business logic like status updates and filtering.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController extends BaseController<Order> {

  private final OrderBusinessService orderBusinessService;
  private final OrganizationRepository organizationRepository;

  public OrderController(OrderService orderService, OrderBusinessService orderBusinessService, 
                        OrganizationRepository organizationRepository) {
    super(orderService);
    this.orderBusinessService = orderBusinessService;
    this.organizationRepository = organizationRepository;
  }

  /**
   * Creates a new order.
   *
   * @param request the order creation request
   * @return the created order
   */
  @PostMapping
  public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request) {
    // TODO: Get organization from security context instead of hardcoding
    Organization organization = organizationRepository.findAll().stream().findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No organization available"));
    
    Order order = orderBusinessService.createOrder(request, organization);
    return ResponseEntity.ok(order);
  }

  /**
   * Updates order status.
   *
   * @param uuid the order UUID
   * @param status the new status
   * @return the updated order
   */
  @PutMapping("/{uuid}/status")
  public ResponseEntity<Order> updateStatus(
      @PathVariable @ValidUuid String uuid, @RequestParam OrderStatus status) {
    Order order = orderBusinessService.updateStatus(uuid, status);
    return ResponseEntity.ok(order);
  }

  /**
   * Finds orders by customer.
   *
   * @param customerId the customer UUID
   * @return list of orders for the customer
   */
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<Order>> findByCustomer(@PathVariable @ValidUuid String customerId) {
    List<Order> orders = orderBusinessService.findByCustomer(customerId);
    return ResponseEntity.ok(orders);
  }

  /**
   * Finds orders by status.
   *
   * @param status the order status
   * @return list of orders with the specified status
   */
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Order>> findByStatus(@PathVariable OrderStatus status) {
    List<Order> orders = orderBusinessService.findByStatus(status);
    return ResponseEntity.ok(orders);
  }

  /**
   * Finds orders by organization.
   *
   * @param organizationId the organization UUID
   * @return list of orders for the organization
   */
  @GetMapping("/organization/{organizationId}")
  public ResponseEntity<List<Order>> findByOrganization(@PathVariable @ValidUuid String organizationId) {
    List<Order> orders = orderBusinessService.findByOrganization(organizationId);
    return ResponseEntity.ok(orders);
  }
}
