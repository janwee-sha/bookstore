package com.janwee.bookstore.book.domain;

import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HibernateCachingTest {
    @Autowired
    private BookRepository bookRepo;

    @Test
    public void givenBooksShouldCacheThem() {
        Book book = new Book();
        book.setAmount(20);
        book.setName("Dummy Book");
        book.setPrice(new BigDecimal("23.5"));
        book.setPublisher("Dummy Publisher");
        book.setPublishBy(LocalDate.now());
        book.setAuthorId(1L);
        bookRepo.save(book);
        bookRepo.findById(book.getId());
        try {
            int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
                    .getCache("com.janwee.bookstore.book.domain.Book").getSize();
            assertThat(size).isGreaterThan(0);
        } finally {
            bookRepo.deleteById(book.getId());
        }
    }
}
