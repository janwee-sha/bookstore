package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.service.BookService;
import com.janwee.bookstore.book.presentation.message.BookResponse;
import com.janwee.bookstore.book.presentation.message.PublishingBookRequest;
import com.janwee.bookstore.book.presentation.message.UpdatingBookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookApplicationService {
    private final BookRepository bookRepo;
    private final BookService bookService;
    private final BookResponseAssembler bookResponseAssembler;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<BookResponse> books(Pageable pageable) {
        log.info("Loading books.");
        Page<Book> books = bookRepo.findAll(pageable);
        return new PageImpl<>(bookResponseAssembler.assemble(books.getContent()), pageable, books.getTotalElements());
    }


    public void hasBookOfId(long id) {
        log.info("Checking existence of book with ID: {}.", id);
        if (!bookRepo.existsById(id)) {
            throw new BookNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    public BookResponse bookOfId(long id) {
        return bookRepo.findById(id).map(bookResponseAssembler::assemble)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(PublishingBookRequest request) {
        log.info("Publishing book.");
        Book book = request.toNewBook();
        bookService.validate(book);
        bookRepo.save(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void change(long id, UpdatingBookRequest request) {
        log.info("Changing book information.");
        Optional<Book> existing = bookRepo.findById(id);
        if (existing.isEmpty()) {
            throw new BookNotFoundException(id);
        }

        Book changedBook = request.changedInfoOf(existing.get());
        bookService.validate(changedBook);
        bookRepo.save(changedBook);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void withdraw(long id) {
        log.info("Removing book with ID: {}.", id);
        if (!bookRepo.existsById(id))
            return;
        bookRepo.deleteById(id);
    }
}
