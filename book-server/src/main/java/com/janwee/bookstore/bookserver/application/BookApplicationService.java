package com.janwee.bookstore.bookserver.application;


import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookApplicationService {
    private final BookRepository bookRepo;
    private final AuthorService authorService;

    @Autowired
    public BookApplicationService(BookRepository bookRepo, AuthorService authorService) {
        this.bookRepo = bookRepo;
        this.authorService = authorService;
    }

    public BookInfo book(Long id) {
        Book book = bookRepo.findById(id);
        if (book == null) {
            return null;
        }
        Author author = authorService.author(book.getAuthorId());
        return new BookInfo(book, author);
    }

    public void add(Book book) {
        bookRepo.add(book);
    }
}
