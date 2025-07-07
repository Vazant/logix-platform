package com.vazant.logix.orders;

import com.vazant.logix.orders.application.service.customer.CustomerService;
import com.vazant.logix.orders.application.service.order.OrderService;
import com.vazant.logix.orders.application.service.user.UserService;
import com.vazant.logix.orders.infrastructure.config.OrdersProperties;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private OrdersProperties ordersProperties;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void shouldLoadAllRequiredBeans() {
		assertThat(applicationContext.getBean(CustomerService.class)).isNotNull();
		assertThat(applicationContext.getBean(OrderService.class)).isNotNull();
		assertThat(applicationContext.getBean(UserService.class)).isNotNull();
	}

	@Test
	void shouldLoadConfigurationProperties() {
		assertThat(ordersProperties).isNotNull();
		assertThat(ordersProperties.getCache()).isNotNull();
		assertThat(ordersProperties.getRetry()).isNotNull();
	}

	@Test
	void shouldLoadDataSources() {
		assertThat(dataSource).isNotNull();
	}

	@Test
	void shouldLoadJpaRepositories() {
		assertThat(customerRepository).isNotNull();
		assertThat(orderRepository).isNotNull();
	}

	@Test
	void shouldLoadTestProfile() {
		assertThat(applicationContext.getEnvironment().getActiveProfiles())
			.contains("test");
	}
}
