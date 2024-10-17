package com.janwee.bookstore.foundation.event;

public interface EventPublisher {
    void publish(Event... events);
}
