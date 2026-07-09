package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.foundation.event.IntegrationEvent;

public interface EventPublisher {
    void publish(IntegrationEvent... events);
}
