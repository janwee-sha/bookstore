package com.janwee.bookstore.order.core.southbound.port;

import com.janwee.bookstore.order.core.domain.Order;
import com.janwee.bookstore.order.core.southbound.message.BookReview;

public interface BookClient {
    BookReview check(Order order);
}
