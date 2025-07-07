package com.vazant.logix.orders.infrastructure.repository;

import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.product.ProductBuilder;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.repository.product.CategoryRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryIntegrationTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Organization organization1;
  private Organization organization2;
  private Category category1;
  private Category category2;
  private Product product1;
  private Product product2;
  private Product product3;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();
    organizationRepository.deleteAll();

    // Create organizations
    organization1 = OrganizationBuilder.organization()
        .name("Test Org 1")
        .email("org1@example.com")
        .address("Address 1")
        .phoneNumber("+1234567890")
        .build();
    organization2 = OrganizationBuilder.organization()
        .name("Test Org 2")
        .email("org2@example.com")
        .address("Address 2")
        .phoneNumber("+1234567891")
        .build();
    organizationRepository.saveAll(List.of(organization1, organization2));

    // Create categories
    category1 = new Category("Electronics", "Electronic devices and accessories");
    category2 = new Category("Books", "Books and publications");
    categoryRepository.saveAll(List.of(category1, category2));

    // Create products
    product1 = ProductBuilder.product()
        .name("Laptop")
        .prices(null)
        .description("High-performance laptop")
        .skuCode("SKU-LAPTOP-001")
        .dimensions(null)
        .stockQuantity(10)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();
    product2 = ProductBuilder.product()
        .name("Smartphone")
        .prices(null)
        .description("Latest smartphone model")
        .skuCode("SKU-PHONE-001")
        .dimensions(null)
        .stockQuantity(25)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();
    product3 = ProductBuilder.product()
        .name("Programming Book")
        .prices(null)
        .description("Java programming guide")
        .skuCode("SKU-BOOK-001")
        .dimensions(null)
        .stockQuantity(50)
        .categoryId(category2.getUuid())
        .imageId(null)
        .organization(organization2)
        .build();

    productRepository.saveAll(List.of(product1, product2, product3));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFindByCategoryUuid() {
    // When
    List<Product> electronicsProducts = productRepository.findByCategoryUuid(category1.getUuid());
    List<Product> booksProducts = productRepository.findByCategoryUuid(category2.getUuid());

    // Then
    assertThat(electronicsProducts).hasSize(2);
    assertThat(electronicsProducts).allMatch(p -> p.getCategoryId().equals(category1.getUuid()));
    assertThat(electronicsProducts).extracting("name").containsExactlyInAnyOrder("Laptop", "Smartphone");

    assertThat(booksProducts).hasSize(1);
    assertThat(booksProducts.get(0).getName()).isEqualTo("Programming Book");
  }

  @Test
  void shouldReturnEmptyListWhenCategoryNotFound() {
    // When
    List<Product> found = productRepository.findByCategoryUuid(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByOrganizationUuid() {
    // When
    List<Product> org1Products = productRepository.findByOrganizationUuid(organization1.getUuid());
    List<Product> org2Products = productRepository.findByOrganizationUuid(organization2.getUuid());

    // Then
    assertThat(org1Products).hasSize(2);
    assertThat(org1Products).allMatch(p -> p.getOrganization().getUuid().equals(organization1.getUuid()));
    assertThat(org1Products).extracting("name").containsExactlyInAnyOrder("Laptop", "Smartphone");

    assertThat(org2Products).hasSize(1);
    assertThat(org2Products.get(0).getName()).isEqualTo("Programming Book");
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationNotFound() {
    // When
    List<Product> found = productRepository.findByOrganizationUuid(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindAllProducts() {
    // When
    List<Product> allProducts = productRepository.findAll();

    // Then
    assertThat(allProducts).hasSize(3);
    assertThat(allProducts).extracting("name")
        .containsExactlyInAnyOrder("Laptop", "Smartphone", "Programming Book");
  }

  @Test
  void shouldSaveProduct() {
    // Given
    Product newProduct = ProductBuilder.product()
        .name("New Product")
        .prices(null)
        .description("New product description")
        .skuCode("SKU-NEW-001")
        .dimensions(null)
        .stockQuantity(15)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(newProduct);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUuid()).isNotNull();
    assertThat(saved.getName()).isEqualTo("New Product");
    assertThat(saved.getSkuCode()).isEqualTo("SKU-NEW-001");
    assertThat(saved.getCategoryId()).isEqualTo(category1.getUuid());
    assertThat(saved.getOrganization().getUuid()).isEqualTo(organization1.getUuid());
  }

  @Test
  void shouldUpdateProduct() {
    // Given
    Product product = productRepository.findAll().get(0);
    product.setName("Updated Product");
    product.setDescription("Updated description");
    product.setStockQuantity(100);

    // When
    Product updated = productRepository.save(product);

    // Then
    assertThat(updated.getName()).isEqualTo("Updated Product");
    assertThat(updated.getDescription()).isEqualTo("Updated description");
    assertThat(updated.getStockQuantity()).isEqualTo(100);
    
    // Verify in database
    Product found = productRepository.findById(product.getUuid()).orElseThrow();
    assertThat(found.getName()).isEqualTo("Updated Product");
    assertThat(found.getDescription()).isEqualTo("Updated description");
  }

  @Test
  void shouldDeleteProduct() {
    // Given
    Product product = productRepository.findAll().get(0);
    UUID productUuid = product.getUuid();

    // When
    productRepository.deleteById(productUuid);

    // Then
    assertThat(productRepository.findById(productUuid)).isEmpty();
    assertThat(productRepository.count()).isEqualTo(2);
  }

  @Test
  void shouldCountProducts() {
    // When
    long count = productRepository.count();

    // Then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void shouldCheckIfProductExists() {
    // Given
    Product product = productRepository.findAll().get(0);

    // When
    boolean exists = productRepository.existsById(product.getUuid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldCheckIfProductNotExists() {
    // When
    boolean exists = productRepository.existsById(UUID.randomUUID());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFindById() {
    // Given
    Product product = productRepository.findAll().get(0);

    // When
    Product found = productRepository.findById(product.getUuid()).orElseThrow();

    // Then
    assertThat(found.getUuid()).isEqualTo(product.getUuid());
    assertThat(found.getName()).isEqualTo(product.getName());
  }

  @Test
  void shouldReturnEmptyWhenIdNotFound() {
    // When
    var found = productRepository.findById(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldHandleProductWithNullDescription() {
    // Given
    Product product = ProductBuilder.product()
        .name("No Description Product")
        .prices(null)
        .description(null)
        .skuCode("SKU-NO-DESC-001")
        .dimensions(null)
        .stockQuantity(5)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(product);

    // Then
    assertThat(saved.getDescription()).isNull();
    assertThat(saved.getName()).isEqualTo("No Description Product");
  }

  @Test
  void shouldHandleProductWithZeroStock() {
    // Given
    Product product = ProductBuilder.product()
        .name("Zero Stock Product")
        .prices(null)
        .description("Out of stock")
        .skuCode("SKU-ZERO-001")
        .dimensions(null)
        .stockQuantity(0)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(product);

    // Then
    assertThat(saved.getStockQuantity()).isEqualTo(0);
    assertThat(saved.getName()).isEqualTo("Zero Stock Product");
  }

  @Test
  void shouldHandleProductWithNullCategory() {
    // Given
    Product product = ProductBuilder.product()
        .name("No Category Product")
        .prices(null)
        .description("No category")
        .skuCode("SKU-NO-CAT-001")
        .dimensions(null)
        .stockQuantity(10)
        .categoryId(null)
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(product);

    // Then
    assertThat(saved.getCategoryId()).isNull();
    assertThat(saved.getName()).isEqualTo("No Category Product");
  }

  @Test
  void shouldHandleProductWithNullImage() {
    // Given
    Product product = ProductBuilder.product()
        .name("No Image Product")
        .prices(null)
        .description("No image")
        .skuCode("SKU-NO-IMG-001")
        .dimensions(null)
        .stockQuantity(10)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(product);

    // Then
    assertThat(saved.getImageId()).isNull();
    assertThat(saved.getName()).isEqualTo("No Image Product");
  }

  @Test
  void shouldHandleProductWithNullDimensions() {
    // Given
    Product product = ProductBuilder.product()
        .name("No Dimensions Product")
        .prices(null)
        .description("No dimensions")
        .skuCode("SKU-NO-DIM-001")
        .dimensions(null)
        .stockQuantity(10)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();

    // When
    Product saved = productRepository.save(product);

    // Then
    assertThat(saved.getDimensions()).isNull();
    assertThat(saved.getName()).isEqualTo("No Dimensions Product");
  }

  @Test
  void shouldFindProductsByMultipleCategories() {
    // Given - create more products in different categories
    Product electronicsProduct = ProductBuilder.product()
        .name("Tablet")
        .prices(null)
        .description("Tablet device")
        .skuCode("SKU-TABLET-001")
        .dimensions(null)
        .stockQuantity(15)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();
    Product bookProduct = ProductBuilder.product()
        .name("Design Book")
        .prices(null)
        .description("Design patterns book")
        .skuCode("SKU-BOOK-002")
        .dimensions(null)
        .stockQuantity(30)
        .categoryId(category2.getUuid())
        .imageId(null)
        .organization(organization2)
        .build();
    
    productRepository.saveAll(List.of(electronicsProduct, bookProduct));

    // When
    List<Product> electronicsProducts = productRepository.findByCategoryUuid(category1.getUuid());
    List<Product> booksProducts = productRepository.findByCategoryUuid(category2.getUuid());

    // Then
    assertThat(electronicsProducts).hasSize(3); // Laptop, Smartphone, Tablet
    assertThat(booksProducts).hasSize(2); // Programming Book, Design Book
  }

  @Test
  void shouldFindProductsByMultipleOrganizations() {
    // Given - create more products in different organizations
    Product org1Product = ProductBuilder.product()
        .name("Org1 Product")
        .prices(null)
        .description("Organization 1 product")
        .skuCode("SKU-ORG1-001")
        .dimensions(null)
        .stockQuantity(20)
        .categoryId(category1.getUuid())
        .imageId(null)
        .organization(organization1)
        .build();
    Product org2Product = ProductBuilder.product()
        .name("Org2 Product")
        .prices(null)
        .description("Organization 2 product")
        .skuCode("SKU-ORG2-001")
        .dimensions(null)
        .stockQuantity(25)
        .categoryId(category2.getUuid())
        .imageId(null)
        .organization(organization2)
        .build();
    
    productRepository.saveAll(List.of(org1Product, org2Product));

    // When
    List<Product> org1Products = productRepository.findByOrganizationUuid(organization1.getUuid());
    List<Product> org2Products = productRepository.findByOrganizationUuid(organization2.getUuid());

    // Then
    assertThat(org1Products).hasSize(3); // Laptop, Smartphone, Org1 Product
    assertThat(org2Products).hasSize(2); // Programming Book, Org2 Product
  }
} 