package com.janwee.bookstore.book.domain.service;

import com.janwee.bookstore.book.domain.exception.InvalidBookException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookPublicationPolicy {
    private final AuthorRepository authorRepo;

    @Autowired
    public BookPublicationPolicy(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    public void check(Book book) {
        if (book.authorId() != null
                && !authorRepo.hasAuthorOf(book.authorId())) {
            throw InvalidBookException.unavailableAuthor();
        }
    }
}
