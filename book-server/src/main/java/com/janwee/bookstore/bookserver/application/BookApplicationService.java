package com.janwee.bookstore.bookserver.application;

import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.domain.Book;
import com.janwee.bookstore.bookserver.domain.BookRepository;
import com.janwee.bookstore.common.domain.exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<BookInfo> books(Pageable page) {
        log.info("Loading books.");
        Page<Book> books = bookRepo.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize(),
                Sort.by("id").descending()));
        return new PageImpl<>(books.stream().map(book -> new BookInfo(book, authorService.author(book.getAuthorId())))
                .collect(Collectors.toList()), page, books.getTotalElements());
    }


    private Optional<BookInfo> book(Long id) {
        log.info("Loading book with ID: {}.", id);
        return bookRepo.findById(id).map(book -> new BookInfo(book, authorService.author(book.getAuthorId())));
    }

    public void checkExistenceOfBook(Long id) {
        log.info("Checking existence of book with ID: {}.", id);
        if (!bookRepo.existsById(id)) {
            throw new HttpException("Book not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public BookInfo nonNullBook(Long id) {
        return book(id).orElseThrow(() -> new HttpException("Book not found", HttpStatus.NOT_FOUND));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(Book book) {
        log.info("Adding book.");
        book.setId(null);
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void edit(Book book) {
        log.info("Modifying book.");
        throwIfBookNotFound(book.getId());
        bookRepo.save(book);
    }

    private void throwIfBookNotFound(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new HttpException("Book not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void remove(Long id) {
        log.info("Removing book with ID: {}.", id);
        throwIfBookNotFound(id);
        bookRepo.deleteById(id);
    }
}
