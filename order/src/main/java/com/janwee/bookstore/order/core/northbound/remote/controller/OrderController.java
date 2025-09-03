package com.janwee.bookstore.order.core.northbound.remote.controller;

import com.janwee.bookstore.order.core.domain.Order;
import com.janwee.bookstore.order.core.domain.Order_;
import com.janwee.bookstore.order.core.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.core.northbound.message.OrderingBookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("orders")
@Tag(name = "Order resource")
@Validated
@Slf4j
public class OrderController {
    private final OrderApplicationService orderAppService;

    @Autowired
    public OrderController(OrderApplicationService orderAppService) {
        this.orderAppService = orderAppService;
    }

    @GetMapping
    @Operation(description = "Retrieve all orders")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    public Page<Order> orders(@SortDefault.SortDefaults({@SortDefault(sort = Order_.CREATED_AT,
            direction = Sort.Direction.DESC)}) Pageable page) {
        return orderAppService.orders(page);
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieve details for order of given ID")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    public Order order(@PathVariable long id) {
        return orderAppService.nonNullOrderOfId(id);
    }

    @PostMapping
    @Operation(description = "Order a book")
    public ResponseEntity<Void> orderBook(@RequestBody OrderingBookRequest request) {
        long newOrderId = orderAppService.orderBook(request);
        String newOrderLocation = "/orders/" + newOrderId;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(newOrderLocation));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
