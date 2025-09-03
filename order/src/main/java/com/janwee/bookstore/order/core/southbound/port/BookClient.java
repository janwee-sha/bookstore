package com.janwee.bookstore.order.core.southbound.port;

import com.janwee.bookstore.order.core.domain.Order;
import com.janwee.bookstore.order.core.southbound.message.BookReview;

/**
 * TODO Add a description for the class here
 *
 * @author Will Hsia
 * @since 2024/6/13
 */
public interface BookClient {
    BookReview check(Order order);
}
