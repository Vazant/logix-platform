package com.vazant.logix.currency.infrastructure.config;

import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;

/**
 * Kafka configuration for currency service messaging.
 * <p>
 * Configures producer and consumer factories, templates, and deserializers for currency conversion messages.
 */
@EnableKafka
@Configuration
@EnableConfigurationProperties(CurrencyProperties.class)
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  private final CurrencyProperties properties;

  /**
   * Constructs a new KafkaConfig with the given currency properties.
   *
   * @param properties the currency configuration properties
   */
  public KafkaConfig(CurrencyProperties properties) {
    this.properties = properties;
  }

  /**
   * Producer factory for currency conversion response messages.
   *
   * @return the producer factory
   */
  @Bean
  public ProducerFactory<String, CurrencyConversionResponse> responseProducerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, CurrencyConversionResponse> kafkaTemplate() {
    return new KafkaTemplate<>(responseProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, CurrencyConversionRequest> requestConsumerFactory() {
    JsonDeserializer<CurrencyConversionRequest> deserializer =
        new JsonDeserializer<>(CurrencyConversionRequest.class);
    deserializer.setRemoveTypeHeaders(false);
    deserializer.addTrustedPackages("com.vazant.logix.shared.kafka.dto");

    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getKafka().getGroupId());
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
  }
}
