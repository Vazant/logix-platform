package com.vazant.logix.orders.infrastructure.repository.order;

import com.vazant.logix.orders.domain.order.Item;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, UUID> {}
