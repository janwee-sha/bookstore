package com.janwee.bookstore.book.infrastructure.persistence;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.AuthorPOJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorRepositoryJpaImplUnitTest {

    @Mock
    private AuthorPOJpaRepository jpaRepo;

    @InjectMocks
    private AuthorRepositoryJpaImpl adapter;

    @Test
    void shouldRejectAddingAuthorWithExistingId() {
        Author author = newAuthor();
        author.assignId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.add(author));

        assertEquals("New author must not already have an ID", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingAuthorWithoutId() {
        Author author = newAuthor();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(author));

        assertEquals("Existing author ID is required for update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdatingMissingAuthor() {
        Author author = newAuthor();
        author.assignId(1L);
        when(jpaRepo.existsById(1L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> adapter.update(author));

        assertEquals("Existing author must be present before update", ex.getMessage());
        verify(jpaRepo, never()).save(any());
    }

    private static Author newAuthor() {
        return Author.builder()
                .name("author_a")
                .profile("profile_a")
                .phoneNumber("123456789")
                .build();
    }
}
