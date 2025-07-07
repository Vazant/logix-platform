package com.vazant.logix.orders.domain.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Utility component for publishing domain events using Spring's ApplicationEventPublisher.
 * <p>
 * Allows static publishing of events from anywhere in the application.
 */
@Component
public class DomainEventPublisher {

  private static ApplicationEventPublisher publisher;

  /**
   * Initializes the static event publisher.
   *
   * @param publisher the Spring ApplicationEventPublisher
   */
  public DomainEventPublisher(ApplicationEventPublisher publisher) {
    DomainEventPublisher.publisher = publisher;
  }

  /**
   * Publishes a domain event if the publisher is initialized.
   *
   * @param event the event to publish
   */
  public static void publish(Object event) {
    if (publisher != null) {
      publisher.publishEvent(event);
    }
  }
}
