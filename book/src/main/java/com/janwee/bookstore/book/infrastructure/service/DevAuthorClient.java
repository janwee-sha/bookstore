package com.janwee.bookstore.book.infrastructure.service;

import com.janwee.bookstore.book.domain.Author;
import com.janwee.bookstore.book.domain.AuthorClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("dev")
@Service
public class DevAuthorClient implements AuthorClient {
    @PostConstruct
    public void init() {
        System.out.println("Using DevAuthorClient as the implementation of AuthorService.");
    }

    @Override
    public Author author(Long authorId) {
        return new Author(-1L, "A dummy author for development environment testing.",
                "A dummy author for development environment testing.");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Destroying the DevAuthorClient Bean.");
    }

}
