package com.janwee.bookstore.book.core.domain.event;

public interface OrderEventConsumer {
    void onOrderCreated(OrderCreated event);
}
