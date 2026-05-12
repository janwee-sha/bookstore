package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookRepository {
    Page<Book> findAll(Pageable pageable);

    Optional<Book> findById(Long id);

    boolean existsById(Long id);

    Book save(Book book);

    void deleteById(Long id);
}
