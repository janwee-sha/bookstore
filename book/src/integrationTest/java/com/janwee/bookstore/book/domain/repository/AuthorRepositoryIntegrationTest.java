package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Author;
import com.janwee.bookstore.book.core.domain.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BookJpaTestConfiguration.class)
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSavingAuthor() {
        Author author1 = new Author.Builder().ofName("author_a").withProfile("profile_a").withPhoneNumber("123456789").build();

        authorRepo.add(author1);
        entityManager.flush();
        entityManager.clear();

        Optional<Author> author2 = authorRepo.authorOf(author1.id());
        assertTrue(author2.isPresent());
        assertAll(
                () -> assertNotNull(author1.id()),
                () -> assertEquals(author1.name(), author2.get().name()),
                () -> assertEquals(author1.profile(), author2.get().profile()),
                () -> assertEquals(author1.phoneNumber(), author2.get().phoneNumber())
        );
    }
}
