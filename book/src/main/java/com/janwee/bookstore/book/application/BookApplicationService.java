package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.service.BookService;
import com.janwee.bookstore.book.resource.message.BookResponse;
import com.janwee.bookstore.book.resource.message.PublishingBookRequest;
import com.janwee.bookstore.book.resource.message.UpdatingBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BookApplicationService {
    private final BookRepository bookRepo;
    private final BookService bookService;
    private final BookResponseAssembler bookResponseAssembler;

    @Autowired
    public BookApplicationService(BookRepository bookRepo, BookService bookService,
                                  BookResponseAssembler bookResponseAssembler) {
        this.bookRepo = bookRepo;
        this.bookService = bookService;
        this.bookResponseAssembler = bookResponseAssembler;
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<BookResponse> books(Pageable page) {
        log.info("Loading books.");
        Page<Book> books = bookRepo.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize(),
                Sort.by("id").descending()));
        return new PageImpl<>(bookResponseAssembler.assemble(books.getContent()), page, books.getTotalElements());
    }


    public void checkExistenceOfBook(Long id) {
        log.info("Checking existence of book with ID: {}.", id);
        if (!bookRepo.existsById(id)) {
            throw new BookNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public BookResponse bookOfId(Long id) {
        return bookRepo.findById(id).map(bookResponseAssembler::assemble).orElseThrow(BookNotFoundException::new);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(PublishingBookRequest request) {
        log.info("Publishing book.");
        Book book = request.toBook();
        bookService.validate(book);
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update(UpdatingBookRequest request) {
        log.info("Modifying book.");
        Book book = request.toBook();
        bookService.validate(book);
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void withdraw(Long id) {
        log.info("Removing book with ID: {}.", id);
        if (!bookRepo.existsById(id))
            return;
        bookRepo.deleteById(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void sell(Long bookId) {
        Optional<Book> book = bookRepo.findById(bookId);
        if (book.isEmpty()) {
            throw new BookNotFoundException();
        }
        book.get().sell(1);
        bookRepo.saveAndFlush(book.get());
        try {
            TimeUnit.MICROSECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
