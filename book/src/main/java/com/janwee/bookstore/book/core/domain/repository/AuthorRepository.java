package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Author;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> authorOf(Long id);

    List<Author> authorsOf(Collection<Long> ids);

    boolean hasAuthorOf(Long id);

    void add(Author author);

    void update(Author author);
}
