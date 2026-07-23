package com.janwee.bookstore.book.application.event;

import com.janwee.bookstore.foundation.event.IntegrationEvent;

public interface IntegrationEventPublisher {
    void publish(IntegrationEvent... events);
}
