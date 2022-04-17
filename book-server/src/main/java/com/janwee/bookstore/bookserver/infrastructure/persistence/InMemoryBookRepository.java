package com.janwee.bookstore.bookserver.infrastructure.persistence;

import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryBookRepository implements BookRepository {
    private static final List<Book> COLLECTION;

    static {
        COLLECTION = new ArrayList<>();
    }

    @Override
    public Book findById(Long id) {
        for (Book book : COLLECTION) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        return COLLECTION;
    }

    @Override
    public void add(Book book) {
        for (Book elem : COLLECTION) {
            if (elem.getId().equals(book.getId())) {
                throw new RuntimeException("Book with duplicated key exists.");
            }
        }
        COLLECTION.add(book);
    }

    @Override
    public void update(Book book) {
        for (Book elem : COLLECTION) {
            if (elem.getId().equals(book.getId())) {
                BeanUtils.copyProperties(book, elem);
                break;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        COLLECTION.removeIf(book -> book.getId().equals(id));
    }
}
