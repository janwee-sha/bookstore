package com.janwee.bookstore.order.northbound.remote.controller;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.northbound.local.OrderApplicationService;
import com.janwee.bookstore.order.northbound.message.OrderingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation(description = "Orders")
    @PageableAsQueryParam
    public Page<Order> orders(Pageable page) {
        return orderAppService.orders(page);
    }

    @PostMapping
    @Operation(description = "Create an order")
    @ResponseStatus(HttpStatus.OK)
    public void createOrder(@RequestBody OrderingRequest request) {
        orderAppService.createOrder(request);
    }
}
