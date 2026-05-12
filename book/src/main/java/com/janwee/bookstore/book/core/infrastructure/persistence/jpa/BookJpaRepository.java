package com.janwee.bookstore.book.core.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.core.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
