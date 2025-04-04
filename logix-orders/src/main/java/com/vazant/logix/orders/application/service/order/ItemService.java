package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.order.Item;
import com.vazant.logix.orders.infrastructure.repository.order.ItemRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractCrudService<Item> {
  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    super(Item.class);
    this.itemRepository = itemRepository;
  }

  @Override
  protected JpaRepository<Item, UUID> getRepository() {
    return itemRepository;
  }
}
