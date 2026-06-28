package com.janwee.bookstore.book.domain.service;

import com.janwee.bookstore.foundation.event.Event;

public interface EventPublisher {
    void publish(Event... events);
}
