package integration.com.janwee.bookstore.book;

import com.janwee.bookstore.book.BookApplication;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.book.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = BookApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookRepositoryIntegrationTest {
    @Autowired
    private BookRepository bookRepo;

    @Test
    void testSavingBook() {
        Book book1 = new Book.Builder()
                .withName("author_a")
                .withAmount(1)
                .withPrice(Price.of(Currency.USD, BigDecimal.TEN))
                .byPublisher("publisher-a")
                .byAuthor(1L)
                .build();

        bookRepo.save(book1);
        bookRepo.flush();

        Optional<Book> book2 = bookRepo.findById(book1.id());
        assertTrue(book2.isPresent());
        assertEquals(book1.name(), book2.get().name());
        assertEquals(book1.amount(), book2.get().amount());
        assertEquals(book1.price(), book2.get().price());
        assertEquals(book1.publisher(), book2.get().publisher());
        assertEquals(book1.authorId(), book2.get().authorId());
    }
}
