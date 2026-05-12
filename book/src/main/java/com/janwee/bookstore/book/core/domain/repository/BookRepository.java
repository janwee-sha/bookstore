package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
