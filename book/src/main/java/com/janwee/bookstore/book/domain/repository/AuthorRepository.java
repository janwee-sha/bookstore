package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
