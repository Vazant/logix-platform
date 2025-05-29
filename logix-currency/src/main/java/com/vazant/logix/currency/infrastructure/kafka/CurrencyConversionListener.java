package com.vazant.logix.currency.infrastructure.kafka;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyConversionListener {

  private final CurrencyApplicationService service;
  private final KafkaTemplate<String, CurrencyConversionResponse> kafkaTemplate;
  private final CurrencyProperties properties;

  @KafkaListener(topics = "${currency.kafka.request-topic}", groupId = "currency-service")
  public void handleConversionRequest(
      CurrencyConversionRequest request,
      @Header(KafkaHeaders.REPLY_TOPIC) String replyTopicHeader,
      @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationIdBytes) {

    log.info(
        "📥 Получен Kafka-запрос: {} → {}, amount = {}",
        request.from(),
        request.to(),
        request.amount());

    BigDecimal result = service.convert(request.from(), request.to(), request.amount());
    CurrencyConversionResponse response = new CurrencyConversionResponse(result);

    String replyTopic =
        replyTopicHeader != null
            ? replyTopicHeader
            : properties.getKafka().getReplyTopicPrefix() + new String(correlationIdBytes);

    ProducerRecord<String, CurrencyConversionResponse> record =
        new ProducerRecord<>(replyTopic, response);
    record.headers().add(KafkaHeaders.CORRELATION_ID, correlationIdBytes);
    kafkaTemplate.send(record);
    log.info(
        "📤 Ответ отправлен в Kafka: {} (correlation-id = {})",
        replyTopic,
        new String(correlationIdBytes));
  }
}
