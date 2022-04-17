package com.janwee.bookstore.bookserver.infrastructure.service;

import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.infrastructure.feign.AuthorFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslatingAuthorService implements AuthorService {
    private final AuthorFeignClient authorFeignClient;

    @Autowired
    public TranslatingAuthorService(AuthorFeignClient authorFeignClient) {
        this.authorFeignClient = authorFeignClient;
    }

    @Override
    public Author author(Long authorId) {
        return authorFeignClient.author(authorId);
    }
}
