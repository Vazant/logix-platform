package com.vazant.logix.shared.kafka.config;

import com.vazant.logix.shared.Constants;

/**
 * Shared Kafka topic names for currency-related messaging.
 * <p>
 * Provides topic names for requests and replies, referencing shared constants.
 */
public class KafkaTopics {
  /**
   * Kafka topic for currency conversion requests.
   */
  public static final String CURRENCY_REQUESTS = Constants.CURRENCY_REQUESTS;
  /**
   * Kafka topic for currency conversion replies.
   */
  public static final String CURRENCY_REPLIES = Constants.CURRENCY_REPLIES;

  /**
   * Default constructor.
   */
  public KafkaTopics() {}
}
