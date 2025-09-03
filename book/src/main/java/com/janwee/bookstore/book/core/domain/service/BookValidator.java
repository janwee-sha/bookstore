package com.janwee.bookstore.book.core.domain.service;

import com.janwee.bookstore.book.core.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.core.domain.exception.InvalidBookException;
import com.janwee.bookstore.book.core.domain.model.Book;
import com.janwee.bookstore.book.core.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.core.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookValidator {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    @Autowired
    public BookValidator(BookRepository bookRepo, AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public void validate(Book book) {
        if (book.id() != null && !bookRepo.existsById(book.id())) {
            throw new BookNotFoundException(book.id());
        }
        if (book.authorId() != null
                && !authorRepo.existsById(book.authorId())) {
            throw InvalidBookException.unavailableAuthor();
        }
    }
}
