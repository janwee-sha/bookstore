package com.janwee.bookstore.book.domain.event;

public interface EventSubscriber {
    void onOrderCreated(OrderCreated event);
}
