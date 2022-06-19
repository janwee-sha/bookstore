package com.janwee.bookstore.order.infrastructure.feign;

import com.janwee.bookstore.order.domain.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("book-server")
public interface BookFeignClient {
    @GetMapping(value = "books/{id}")
    Book book(@PathVariable(name = "id") String bookId);
}
