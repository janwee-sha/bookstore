package com.janwee.bookstore.book.core.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.core.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
