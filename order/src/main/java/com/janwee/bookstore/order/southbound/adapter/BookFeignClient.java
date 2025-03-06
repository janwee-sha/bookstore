package com.janwee.bookstore.order.southbound.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("book")
public interface BookFeignClient {
    @RequestMapping(method = RequestMethod.HEAD, value = "books/{id}")
    ResponseEntity<Void> checkBook(@PathVariable final Long id);
}