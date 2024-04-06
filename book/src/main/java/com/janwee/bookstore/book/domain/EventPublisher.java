package com.janwee.bookstore.book.domain;

public interface EventPublisher {
    void publish(String eventType, Event... events);
}
