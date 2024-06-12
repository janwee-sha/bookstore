package com.janwee.bookstore.book.domain;

import com.janwee.bookstore.book.application.BookApplicationService;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class JPALockingTest {
    private final BookRepository bookRepo;
    private final BookApplicationService bookAppService;

    private Long testBookId;

    @Autowired
    public JPALockingTest(BookRepository bookRepo, BookApplicationService bookAppService) {
        this.bookRepo = bookRepo;
        this.bookAppService = bookAppService;
    }

    @BeforeEach
    @Transactional
    public void beforeEach() {
        Book testBook1 = new Book();
        testBook1.setPrice(new BigDecimal(20));
        testBook1.setName("Test Book 1");
        testBook1.setAmount(1);
        testBook1.setAuthorId(0L);
        testBook1.setPublisher("Test Publisher 1");
        testBookId = bookRepo.saveAndFlush(testBook1).getId();
    }

    @Test
    public void testSellBook() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            bookAppService.sell(testBookId);
            latch.countDown();
        }).start();
        new Thread(() -> {
            bookAppService.sell(testBookId);
            latch.countDown();
        }).start();
        latch.await();
        Optional<Book> soldBook = bookRepo.findById(testBookId);
        System.out.println("------------------");
        System.out.println("The final amount is: " + soldBook.map(Book::getAmount)
                .orElse(null));
        System.out.println("-------------------");
    }

    @AfterEach
    public void afterEach() {
        bookRepo.deleteById(testBookId);
    }
}
