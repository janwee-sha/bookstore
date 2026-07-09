package com.janwee.bookstore.book.domain.service;

import com.janwee.bookstore.foundation.event.IntegrationEvent;

public interface EventPublisher {
    void publish(IntegrationEvent... events);
}
