package com.janwee.bookstore.book.domain;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.BookRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

//@SpringBootTest
public class HibernateCachingTest {
    //    @Autowired
    private BookRepository bookRepo;

    //    @Test
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
//            int size = CacheManager.ALL_CACHE_MANAGERS.get(0)
//                    .getCache("com.janwee.bookstore.book.domain.Book").getSize();
//            assertThat(size).isGreaterThan(0);
        } finally {
            bookRepo.deleteById(book.getId());
        }
    }
}
