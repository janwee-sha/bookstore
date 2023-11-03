package com.janwee.bookstore.order.resource;

import com.janwee.bookstore.order.application.OrderApplicationService;
import com.janwee.bookstore.order.domain.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("orders")
@Tag(name = "Order resource")
@Validated
@Slf4j
public class OrderResource {
    private final OrderApplicationService orderAppService;

    @Autowired
    public OrderResource(OrderApplicationService orderAppService) {
        this.orderAppService = orderAppService;
    }

    @GetMapping
    @Operation(description = "Orders")
    public Page<Order> orders(Pageable page) {
        return orderAppService.orders(page);
    }

    @PostMapping
    @Operation(description = "Create an order")
    @ResponseStatus(HttpStatus.OK)
    public void createOrder(@RequestParam final Long bookId,
                            @Positive(message = "amount must not less than 1") @RequestParam final int amount) {
        orderAppService.createOrder(bookId, amount);
    }
}
