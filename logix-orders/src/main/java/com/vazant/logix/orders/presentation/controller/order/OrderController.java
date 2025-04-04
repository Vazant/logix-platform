package com.vazant.logix.orders.presentation.controller.order;

import com.vazant.logix.orders.application.service.order.OrderService;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.dto.order.OrderRequest;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = orderService.findAll();
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/{orderUuid}")
  public ResponseEntity<Order> getOrder(@PathVariable String orderUuid) {
    Order order = orderService.findByUuid(orderUuid);
    return ResponseEntity.ok(order);
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
    Order created = orderService.create(orderRequest);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{orderUuid}")
  public ResponseEntity<Order> updateOrder(
      @PathVariable String orderUuid, @RequestBody Order order) {
    Order updated = orderService.update(orderUuid, order);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{orderUuid}")
  public ResponseEntity<Void> deleteOrder(@PathVariable String orderUuid) {
    orderService.delete(orderUuid);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{orderUuid}/total")
  public ResponseEntity<BigDecimal> calculateTotal(
      @PathVariable String orderUuid, @RequestParam String targetCurrency) {
    BigDecimal convertedTotal = orderService.calculateTotalIn(orderUuid, targetCurrency);
    return ResponseEntity.ok(convertedTotal);
  }
}
