package com.janwee.bookstore.bookserver.infrastructure.service;

import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.infrastructure.feign.AuthorFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class TranslatingAuthorService implements AuthorService {
    private final AuthorFeignClient authorFeignClient;

    @Autowired
    public TranslatingAuthorService(AuthorFeignClient authorFeignClient) {
        this.authorFeignClient = authorFeignClient;
    }

    @Override
    @HystrixCommand
    public Author author(Long authorId) {
        randomlyRunLong();
        return authorFeignClient.author(authorId);
    }

    private void randomlyRunLong() {
        if (new Random().nextInt(3) + 1 == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
