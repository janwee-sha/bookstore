package com.janwee.bookstore.order.resource;

import com.janwee.bookstore.order.application.OrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@Tag(name = "Order resource")
@Validated
public class OrderResource {
    private final OrderApplicationService orderAppService;

    @Autowired
    public OrderResource(OrderApplicationService orderAppService) {
        this.orderAppService = orderAppService;
    }

    @PostMapping
    @Operation(description = "Create an order")
    @ResponseStatus(HttpStatus.OK)
    public void createOrder() {
        orderAppService.createOrder();
    }
}
