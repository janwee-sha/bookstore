package com.janwee.bookstore.bookserver.application;


import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookApplicationService {
    private static final Logger log = LoggerFactory.getLogger(BookApplicationService.class);
    private final BookRepository bookRepo;
    private final AuthorService authorService;

    @Autowired
    public BookApplicationService(BookRepository bookRepo, AuthorService authorService) {
        this.bookRepo = bookRepo;
        this.authorService = authorService;
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<BookInfo> book(Long id) {
        log.info("Loading book with ID: {}.", id);
        return bookRepo.findById(id).map(book -> new BookInfo(book, authorService.author(book.getAuthorId())));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void add(Book book) {
        log.info("Adding book.");
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void modify(Book book) {
        log.info("Modifying book.");
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remove(Long id) {
        log.info("Removing book with ID: {}.", id);
        bookRepo.deleteById(id);
    }
}
