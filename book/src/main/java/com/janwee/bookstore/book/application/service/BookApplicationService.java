package com.janwee.bookstore.book.application.service;

import com.janwee.bookstore.book.application.assembler.BookViewAssembler;
import com.janwee.bookstore.book.application.command.PublishingBookCommand;
import com.janwee.bookstore.book.application.command.UpdatingBookCommand;
import com.janwee.bookstore.book.application.view.BookView;
import com.janwee.bookstore.book.domain.exception.BookNotFoundException;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import com.janwee.bookstore.book.domain.service.BookPublicationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookApplicationService {
    private final BookRepository bookRepo;
    private final BookPublicationPolicy bookPublicationPolicy;
    private final BookViewAssembler bookViewAssembler;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<BookView> books(Pageable pageable) {
        log.info("Loading books.");
        Page<Book> books = bookRepo.booksOf(pageable);
        return new PageImpl<>(bookViewAssembler.assemble(books.getContent()), pageable, books.getTotalElements());
    }


    @Transactional(readOnly = true)
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
        bookPublicationPolicy.check(book);
        bookRepo.add(book);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void change(long id, UpdatingBookCommand request) {
        log.info("Changing book information.");
        Book existing = bookRepo.bookOf(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Book changedBook = request.toChangedBook(existing);
        bookPublicationPolicy.check(changedBook);
        bookRepo.update(changedBook);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void withdraw(long id) {
        log.info("Removing book: {}.", id);
        if (!bookRepo.hasBookOf(id))
            return;
        bookRepo.delete(id);
    }
}
