package com.janwee.bookstore.authorserver.infrastructure.persistence;

import com.janwee.bookstore.authorserver.domain.Author;
import com.janwee.bookstore.authorserver.domain.AuthorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryAuthorRepository implements AuthorRepository {
    private static final List<Author> COLLECTION;

    static {
        COLLECTION = new ArrayList<>();
    }

    @Override
    public Author findById(Long id) {
        for (Author author : COLLECTION) {
            if (author.getId().equals(id)) {
                return author;
            }
        }
        return null;
    }

    @Override
    public List<Author> findAll() {
        return COLLECTION;
    }

    @Override
    public void add(Author author) {
        for (Author elem : COLLECTION) {
            if (elem.getId().equals(author.getId())) {
                throw new RuntimeException("Author with duplicated key exists.");
            }
        }
        COLLECTION.add(author);
    }

    @Override
    public void update(Author author) {
        for (Author elem : COLLECTION) {
            if (elem.getId().equals(author.getId())) {
                BeanUtils.copyProperties(author, elem);
                break;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        COLLECTION.removeIf(author -> author.getId().equals(id));
    }
}
