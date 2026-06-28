package com.janwee.bookstore.book.domain.event;

import com.janwee.bookstore.foundation.event.Event;

public interface EventPublisher {
    void publish(Event... events);
}
