package com.vazant.logix.orders.infrastructure.kafka;

import com.vazant.logix.shared.kafka.config.KafkaTopics;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

/**
 * Kafka client for sending currency conversion requests and receiving responses using ReplyingKafkaTemplate.
 * Used for integration with the currency microservice via Kafka.
 */
@Component
public class CurrencyConversionClient {

  private final ReplyingKafkaTemplate<String, CurrencyConversionRequest, CurrencyConversionResponse>
      replyingKafkaTemplate;

  /**
   * Constructs a new CurrencyConversionClient.
   *
   * @param replyingKafkaTemplate the Kafka template for sending and receiving messages
   */
  public CurrencyConversionClient(ReplyingKafkaTemplate<String, CurrencyConversionRequest, CurrencyConversionResponse> replyingKafkaTemplate) {
    this.replyingKafkaTemplate = replyingKafkaTemplate;
  }

  public BigDecimal convert(String from, String to, BigDecimal amount) {
    String correlationId = UUID.randomUUID().toString();

    CurrencyConversionRequest request = new CurrencyConversionRequest(from, to, amount);

    ProducerRecord<String, CurrencyConversionRequest> record =
        new ProducerRecord<>(KafkaTopics.CURRENCY_REQUESTS, null, request);
    record
        .headers()
        .add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, KafkaTopics.CURRENCY_REPLIES.getBytes()));
    record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));

    try {
      var future = replyingKafkaTemplate.sendAndReceive(record);
      var response = future.get(10, TimeUnit.SECONDS).value();
      return response.result();
    } catch (Exception e) {
      throw new RuntimeException("❌ Failed to convert currency via Kafka", e);
    }
  }
}
