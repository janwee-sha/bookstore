package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Author;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);

    List<Author> findAllById(Collection<Long> ids);

    boolean existsById(Long id);

    Author save(Author author);
}
