package com.janwee.bookstore.order.resource;

import com.janwee.bookstore.order.application.OrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@Tag(name = "Order resource")
@Validated
@Slf4j
public class OrderResource {
    private final OrderApplicationService orderAppService;
    private final Source source;

    @Autowired
    public OrderResource(OrderApplicationService orderAppService, Processor source) {
        this.orderAppService = orderAppService;
        this.source = source;
    }

    @PostMapping
    @Operation(description = "Create an order")
    @ResponseStatus(HttpStatus.OK)
    public void createOrder(@RequestParam final String bookId) {
        orderAppService.createOrder(bookId);
    }

    @PostMapping("/greeting")
    @Operation(description = "Greet")
    public void greet(){
        String message="Hello message from Order Service";
        source.output()
                .send(MessageBuilder.withPayload(message)
                        .build());
        log.info("Published message: {}.", message);
    }
}
