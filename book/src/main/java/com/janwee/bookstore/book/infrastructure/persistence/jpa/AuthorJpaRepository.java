package com.janwee.bookstore.book.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
