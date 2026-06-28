package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.foundation.event.Event;

public interface EventPublisher {
    void publish(Event... events);
}
