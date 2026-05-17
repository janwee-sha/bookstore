package com.janwee.bookstore.book.core.domain.repository;

import com.janwee.bookstore.book.core.domain.model.Author;
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
        Author author1 = authorOf("author_a", "profile_a", "123456789");

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

    @Test
    void shouldRejectAddingAuthorWithExistingId() {
        Author author = authorOf("author_a", "profile_a", "123456789");
        authorRepo.add(author);
        entityManager.flush();
        entityManager.clear();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authorRepo.add(author));

        assertEquals("New author must not already have an ID", ex.getMessage());
    }

    @Test
    void shouldUpdateExistingAuthor() {
        Author author = authorOf("author_a", "profile_a", "123456789");
        authorRepo.add(author);
        entityManager.flush();
        entityManager.clear();

        Author changed = authorRepo.authorOf(author.id()).orElseThrow();
        changed.setName("author_b");
        changed.setProfile("profile_b");
        changed.setPhoneNumber("987654321");

        authorRepo.update(changed);
        entityManager.flush();
        entityManager.clear();

        Optional<Author> updated = authorRepo.authorOf(author.id());
        assertTrue(updated.isPresent());
        assertAll(
                () -> assertEquals("author_b", updated.get().name()),
                () -> assertEquals("profile_b", updated.get().profile()),
                () -> assertEquals("987654321", updated.get().phoneNumber())
        );
    }

    @Test
    void shouldRejectUpdatingAuthorWithoutId() {
        Author author = authorOf("author_a", "profile_a", "123456789");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authorRepo.update(author));

        assertEquals("Existing author ID is required for update", ex.getMessage());
    }

    @Test
    void shouldRejectUpdatingMissingAuthor() {
        Author author = authorOf("author_a", "profile_a", "123456789");
        author.setId(999999L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authorRepo.update(author));

        assertEquals("Existing author must be present before update", ex.getMessage());
    }

    private static Author authorOf(String name, String profile, String phoneNumber) {
        return new Author.Builder()
                .ofName(name)
                .withProfile(profile)
                .withPhoneNumber(phoneNumber)
                .build();
    }
}
