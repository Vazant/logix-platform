package com.vazant.logix.orders.infrastructure.kafka;

import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyConversionClient {

  private static final String REQUEST_TOPIC = "currency.requests";
  private static final String REPLY_TOPIC = "currency.replies";
  private final ReplyingKafkaTemplate<String, CurrencyConversionRequest, CurrencyConversionResponse>
      replyingKafkaTemplate;

  public BigDecimal convert(String from, String to, BigDecimal amount) {
    String correlationId = UUID.randomUUID().toString();

    CurrencyConversionRequest request = new CurrencyConversionRequest(from, to, amount);

    ProducerRecord<String, CurrencyConversionRequest> record =
        new ProducerRecord<>(REQUEST_TOPIC, null, request);
    record.headers().add(new RecordHeader("reply-topic", REPLY_TOPIC.getBytes()));
    record.headers().add(new RecordHeader("correlation-id", correlationId.getBytes()));

    try {
      var future = replyingKafkaTemplate.sendAndReceive(record);
      var response = future.get(10, TimeUnit.SECONDS).value();
      return response.result();
    } catch (Exception e) {
      throw new RuntimeException("❌ Failed to convert currency via Kafka", e);
    }
  }
}
