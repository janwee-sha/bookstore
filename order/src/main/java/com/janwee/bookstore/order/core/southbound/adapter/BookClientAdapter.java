package com.janwee.bookstore.order.core.southbound.adapter;

import com.janwee.bookstore.order.core.domain.Order;
import com.janwee.bookstore.order.core.southbound.message.BookReview;
import com.janwee.bookstore.order.core.southbound.port.BookClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BookClientAdapter implements BookClient {
    private final BookFeignClient bookFeignClient;

    @Autowired
    public BookClientAdapter(BookFeignClient bookFeignClient) {
        this.bookFeignClient = bookFeignClient;
    }

    @Override
    public BookReview check(Order order) {
        ResponseEntity<Void> entity = bookFeignClient.checkBook(order.bookId());
        if (entity.getStatusCode().is2xxSuccessful()) {
            return BookReview.available();
        }
        return BookReview.unavailable();
    }
}
