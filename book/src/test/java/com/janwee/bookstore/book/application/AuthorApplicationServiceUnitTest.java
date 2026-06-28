package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.application.command.RegisteringAuthorCommand;
import com.janwee.bookstore.book.application.service.AuthorApplicationService;
import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorApplicationServiceUnitTest {

    @Mock
    private AuthorRepository authorRepo;

    @InjectMocks
    private AuthorApplicationService service;

    @Test
    void shouldLoadAuthorById() {
        Author author = newAuthor();
        when(authorRepo.authorOf(1L)).thenReturn(Optional.of(author));

        Optional<Author> result = service.authorOfId(1L);

        assertSame(author, result.orElseThrow());
    }

    @Test
    void shouldRegisterAuthor() {
        RegisteringAuthorCommand request = new RegisteringAuthorCommand();
        request.setName("author_a");
        request.setProfile("profile_a");
        request.setPhoneNumber("123456789");

        service.register(request);

        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepo).add(captor.capture());
        assertEquals("author_a", captor.getValue().name());
        assertEquals("profile_a", captor.getValue().profile());
        assertEquals("123456789", captor.getValue().phoneNumber());
    }

    private static Author newAuthor() {
        return Author.builder()
                .name("author_a")
                .profile("profile_a")
                .phoneNumber("123456789")
                .build();
    }
}
