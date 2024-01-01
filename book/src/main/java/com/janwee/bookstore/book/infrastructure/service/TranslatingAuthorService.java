package com.janwee.bookstore.book.infrastructure.service;

import com.janwee.bookstore.book.domain.Author;
import com.janwee.bookstore.book.domain.AuthorService;
import com.janwee.bookstore.book.infrastructure.feign.AuthorFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Random;

@Profile("prod")
@Service
public class TranslatingAuthorService implements AuthorService {
    private final AuthorFeignClient authorFeignClient;

    @Autowired
    public TranslatingAuthorService(AuthorFeignClient authorFeignClient) {
        this.authorFeignClient = authorFeignClient;
    }

    @Override
    public Author author(Long authorId) {
        randomlyRunLong();
        return authorFeignClient.author(authorId);
    }

    private Author fallbackAuthor(Long authorId) {
        return new Author().withId(authorId).withName("Sorry no author currently available");
    }

    private void randomlyRunLong() {
        if (new Random().nextInt(3) + 1 == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
