package com.janwee.bookstore.bookserver.application;


import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookApplicationService {
    private final BookRepository bookRepo;
    private final AuthorService authorService;

    @Autowired
    public BookApplicationService(BookRepository bookRepo, AuthorService authorService) {
        this.bookRepo = bookRepo;
        this.authorService = authorService;
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<BookInfo> book(Long id) {
        return bookRepo.findById(id).map(book -> new BookInfo(book, authorService.author(book.getAuthorId())));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void add(Book book) {
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remove(Long id) {
        bookRepo.deleteById(id);
    }
}
