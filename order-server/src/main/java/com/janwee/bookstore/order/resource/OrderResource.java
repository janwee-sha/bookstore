package com.janwee.bookstore.order.resource;

import com.janwee.bookstore.order.application.OrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Operation(description = "Create an order")
    @ResponseStatus(HttpStatus.OK)
    public void createOrder(@RequestParam final String bookId) {
        orderAppService.createOrder(bookId);
    }
}
