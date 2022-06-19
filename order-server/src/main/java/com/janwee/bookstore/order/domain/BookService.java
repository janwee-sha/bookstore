package com.janwee.bookstore.order.domain;

import java.util.Optional;

public interface BookService {
    Optional<Book> book(String bookId);
}
