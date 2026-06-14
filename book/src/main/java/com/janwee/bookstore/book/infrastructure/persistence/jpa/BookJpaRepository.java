package com.janwee.bookstore.book.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
