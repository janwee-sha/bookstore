package com.janwee.bookstore.book;

import com.janwee.bookstore.book.core.domain.model.Author;
import com.janwee.bookstore.book.core.domain.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = BookApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private AuthorRepository authorRepo;

    @Test
    void testSavingAuthor() {
        Author author1 = new Author.Builder()
                .ofName("author_a")
                .withProfile("profile_a")
                .withPhoneNumber("123456789")
                .build();

        authorRepo.save(author1);
        authorRepo.flush();

        Optional<Author> author2 = authorRepo.findById(author1.id());
        assertTrue(author2.isPresent());
        assertEquals(author1.name(), author2.get().name());
        assertEquals(author1.profile(), author2.get().profile());
        assertEquals(author1.phoneNumber(), author2.get().phoneNumber());
    }
}
