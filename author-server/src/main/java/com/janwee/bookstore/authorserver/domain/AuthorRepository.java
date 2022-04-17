package com.janwee.bookstore.authorserver.domain;

import java.util.List;

public interface AuthorRepository {
    Author findById(Long id);

    List<Author> findAll();

    void add(Author author);

    void update(Author author);

    void deleteById(Long id);
}
