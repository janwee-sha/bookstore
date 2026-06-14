package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookRepository {
    Page<Book> booksOf(Pageable pageable);

    Optional<Book> bookOf(Long id);

    boolean hasBookOf(Long id);

    void add(Book book);

    void update(Book book);

    void delete(Long id);
}
