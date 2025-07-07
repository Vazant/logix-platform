package com.vazant.logix.currency.infrastructure.kafka;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionRequest;
import com.vazant.logix.shared.kafka.dto.CurrencyConversionResponse;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Kafka listener for handling currency conversion requests.
 * <p>
 * Listens for conversion requests, performs conversion, and sends replies to the appropriate topic.
 */
@Component
public class CurrencyConversionListener {

  private static final Logger log = LoggerFactory.getLogger(CurrencyConversionListener.class);

  private final CurrencyApplicationService service;
  private final KafkaTemplate<String, CurrencyConversionResponse> kafkaTemplate;
  private final CurrencyProperties properties;

  /**
   * Constructs a new CurrencyConversionListener.
   *
   * @param service the currency application service
   * @param kafkaTemplate the Kafka template for sending responses
   * @param properties the currency properties
   */
  public CurrencyConversionListener(
      CurrencyApplicationService service,
      KafkaTemplate<String, CurrencyConversionResponse> kafkaTemplate,
      CurrencyProperties properties) {
    this.service = service;
    this.kafkaTemplate = kafkaTemplate;
    this.properties = properties;
  }

  /**
   * Handles incoming currency conversion requests from Kafka, performs the conversion,
   * and sends the response to the reply topic with the correlation ID.
   *
   * @param request the currency conversion request
   * @param replyTopicHeader the reply topic header (may be null)
   * @param correlationIdBytes the correlation ID as a byte array
   */
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
