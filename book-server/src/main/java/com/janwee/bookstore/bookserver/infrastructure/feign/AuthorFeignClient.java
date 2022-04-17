package com.janwee.bookstore.bookserver.infrastructure.feign;

import com.janwee.bookstore.bookserver.domain.Author;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("author-server")
public interface AuthorFeignClient {
    @GetMapping(value = "authors")
    Author author(@RequestParam("id") Long authorId);
}
