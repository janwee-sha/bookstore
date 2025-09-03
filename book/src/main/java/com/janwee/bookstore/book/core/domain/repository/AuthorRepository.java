package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
