package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.order.Item;
import com.vazant.logix.orders.infrastructure.repository.order.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing order items.
 */
@Slf4j
@Service
@Transactional
public class ItemService extends AbstractCrudService<Item> {

  public ItemService(ItemRepository itemRepository) {
    super(itemRepository, Item.class);
  }

  @Override
  protected String getEntityName() {
    return "Item";
  }
}
