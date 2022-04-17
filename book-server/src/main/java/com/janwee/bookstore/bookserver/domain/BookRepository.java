package com.janwee.bookstore.bookserver.domain;

import java.util.List;

public interface BookRepository {
    Book findById(Long id);

    List<Book> findAll();

    void add(Book book);

    void update(Book book);

    void deleteById(Long id);
}
