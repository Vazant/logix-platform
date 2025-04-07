package com.vazant.logix.orders.domain.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

  private static ApplicationEventPublisher publisher;

  public DomainEventPublisher(ApplicationEventPublisher publisher) {
    DomainEventPublisher.publisher = publisher;
  }

  public static void publish(Object event) {
    if (publisher != null) {
      publisher.publishEvent(event);
    }
  }
}
