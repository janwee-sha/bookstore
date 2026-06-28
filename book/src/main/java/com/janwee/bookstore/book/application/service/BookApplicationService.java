package com.janwee.bookstore.book.application.service;

import com.janwee.bookstore.book.application.command.OrderingBookCommand;
import com.janwee.bookstore.book.application.command.PublishingBookCommand;
import com.janwee.bookstore.book.application.command.UpdatingBookCommand;
import com.janwee.bookstore.book.application.view.BookView;
import com.janwee.bookstore.book.application.assembler.BookViewAssembler;
import com.janwee.bookstore.book.domain.event.BookOrdered;
import com.janwee.bookstore.book.domain.event.BookSoldOut;
import com.janwee.bookstore.book.domain.service.EventPublisher;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.service.BookValidator;
import com.janwee.bookstore.foundation.event.Event;
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
    private final BookValidator bookValidator;
    private final BookViewAssembler bookViewAssembler;
    private final EventPublisher eventPublisher;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<BookView> books(Pageable pageable) {
        log.info("Loading books.");
        Page<Book> books = bookRepo.booksOf(pageable);
        return new PageImpl<>(bookViewAssembler.assemble(books.getContent()), pageable, books.getTotalElements());
    }


    public void hasBookOfId(long id) {
        log.info("Checking existence of book: {}.", id);
        if (!bookRepo.hasBookOf(id)) {
            throw new BookNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    public BookView bookOfId(long id) {
        return bookRepo.bookOf(id).map(bookViewAssembler::assemble)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(PublishingBookCommand request) {
        log.info("Publishing book.");
        Book book = request.toNewBook();
        bookValidator.validate(book);
        bookRepo.add(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void change(long id, UpdatingBookCommand request) {
        log.info("Changing book information.");
        Optional<Book> existing = bookRepo.bookOf(id);
        if (existing.isEmpty()) {
            throw new BookNotFoundException(id);
        }

        Book changedBook = request.changedInfoOf(existing.get());
        bookValidator.validate(changedBook);
        bookRepo.update(changedBook);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void withdraw(long id) {
        log.info("Removing book: {}.", id);
        if (!bookRepo.hasBookOf(id))
            return;
        bookRepo.delete(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void order(long id, OrderingBookCommand request) {
        log.info("Ordering book: {}.", id);
        Optional<Book> optBook = bookRepo.bookOf(id);

        if (optBook.isEmpty() || optBook.get().amount() - request.getAmount() < 0) {
            log.info("Book is not found or out of stock.");
            Event bookSoldOut = new BookSoldOut(request.getOrderId(), id);
            eventPublisher.publish(bookSoldOut);
            return;
        }

        Book book = optBook.get();
        book.sell(request.getAmount());
        bookRepo.update(book);
        Event bookOrdered = new BookOrdered(request.getOrderId(), id);
        eventPublisher.publish(bookOrdered);
    }
}
