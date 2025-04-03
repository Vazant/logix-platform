package com.vazant.logix.orders.controller;

import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.dto.OrderRequest;
import com.vazant.logix.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Order> create(@Valid @RequestBody OrderRequest request) {
    Order created = service.createOrder(request);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }
}
