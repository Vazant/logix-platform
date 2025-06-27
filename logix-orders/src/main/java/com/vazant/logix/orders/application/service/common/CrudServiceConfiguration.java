package com.vazant.logix.orders.application.service.common;

import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.media.Image;
import com.vazant.logix.orders.domain.order.Item;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.product.ProductPrice;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.media.ImageRepository;
import com.vazant.logix.orders.infrastructure.repository.order.ItemRepository;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.repository.product.CategoryRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductPriceRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for creating CRUD services automatically.
 * This eliminates the need for individual service classes for basic CRUD operations.
 */
@Configuration
public class CrudServiceConfiguration {

  @Bean
  public CrudService<Customer> customerCrudService(CustomerRepository repository) {
    return new GenericCrudService<>(repository, Customer.class);
  }

  @Bean
  public CrudService<Organization> organizationCrudService(OrganizationRepository repository) {
    return new GenericCrudService<>(repository, Organization.class);
  }

  @Bean
  public CrudService<Product> productCrudService(ProductRepository repository) {
    return new GenericCrudService<>(repository, Product.class);
  }

  @Bean
  public CrudService<Category> categoryCrudService(CategoryRepository repository) {
    return new GenericCrudService<>(repository, Category.class);
  }

  @Bean
  public CrudService<ProductPrice> productPriceCrudService(ProductPriceRepository repository) {
    return new GenericCrudService<>(repository, ProductPrice.class);
  }

  @Bean
  public CrudService<Order> orderCrudService(OrderRepository repository) {
    return new GenericCrudService<>(repository, Order.class);
  }

  @Bean
  public CrudService<Item> itemCrudService(ItemRepository repository) {
    return new GenericCrudService<>(repository, Item.class);
  }

  @Bean
  public CrudService<Image> imageCrudService(ImageRepository repository) {
    return new GenericCrudService<>(repository, Image.class);
  }

  @Bean
  public CrudService<UserGroup> userGroupCrudService(UserGroupRepository repository) {
    return new GenericCrudService<>(repository, UserGroup.class);
  }
} 