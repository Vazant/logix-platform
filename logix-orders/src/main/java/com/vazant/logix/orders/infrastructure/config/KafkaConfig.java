package com.vazant.logix.orders.infrastructure.config;

import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

  @Bean
  public ProducerFactory<String, CurrencyConversionRequest> producerFactory() {
    return new DefaultKafkaProducerFactory<>(
        Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
  }

  @Bean
  public ConsumerFactory<String, CurrencyConversionResponse> consumerFactory() {
    JsonDeserializer<CurrencyConversionResponse> deserializer =
        new JsonDeserializer<>(CurrencyConversionResponse.class);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG,
            "order-service",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class),
        new StringDeserializer(),
        deserializer);
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, CurrencyConversionResponse> repliesContainer(
      ConsumerFactory<String, CurrencyConversionResponse> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, CurrencyConversionResponse> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    return factory.createContainer("currency.replies");
  }

  @Bean
  public ReplyingKafkaTemplate<String, CurrencyConversionRequest, CurrencyConversionResponse>
      replyingKafkaTemplate(
          ProducerFactory<String, CurrencyConversionRequest> pf,
          ConcurrentMessageListenerContainer<String, CurrencyConversionResponse> repliesContainer) {
    return new ReplyingKafkaTemplate<>(pf, repliesContainer);
  }
}
