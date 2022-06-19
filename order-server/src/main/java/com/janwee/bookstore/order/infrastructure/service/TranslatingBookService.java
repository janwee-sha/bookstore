package com.janwee.bookstore.order.infrastructure.service;

import com.janwee.bookstore.order.domain.Book;
import com.janwee.bookstore.order.domain.BookService;
import com.janwee.bookstore.order.infrastructure.feign.BookFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TranslatingBookService implements BookService {
    private final BookFeignClient bookFeignClient;

    @Autowired
    public TranslatingBookService(BookFeignClient bookFeignClient) {
        this.bookFeignClient = bookFeignClient;
    }

    @Override
    public Optional<Book> book(String bookId) {
        try {
            return Optional.ofNullable(bookFeignClient.book(bookId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
