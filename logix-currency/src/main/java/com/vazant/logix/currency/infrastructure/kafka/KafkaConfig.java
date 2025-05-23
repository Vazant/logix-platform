package com.vazant.logix.currency.infrastructure.kafka;

import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaConfig {

  @Bean
  public ProducerFactory<String, CurrencyConversionResponse> responseProducerFactory() {
    return new DefaultKafkaProducerFactory<>(
        Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
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
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(
        Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class,
            ConsumerConfig.GROUP_ID_CONFIG,
            "currency-service-group"),
        new StringDeserializer(),
        deserializer);
  }
}
