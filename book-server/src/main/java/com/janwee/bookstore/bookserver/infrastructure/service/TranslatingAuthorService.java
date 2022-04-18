package com.janwee.bookstore.bookserver.infrastructure.service;

import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.AuthorService;
import com.janwee.bookstore.bookserver.infrastructure.feign.AuthorFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TranslatingAuthorService implements AuthorService {
    private final AuthorFeignClient authorFeignClient;

    @Autowired
    public TranslatingAuthorService(AuthorFeignClient authorFeignClient) {
        this.authorFeignClient = authorFeignClient;
    }

    @Override
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "4000")},
            fallbackMethod = "fallbackAuthor"
    )
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
