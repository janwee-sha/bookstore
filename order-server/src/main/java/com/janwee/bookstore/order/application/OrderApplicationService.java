package com.janwee.bookstore.order.application;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderCreated;
import com.janwee.bookstore.order.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderApplicationService {
    private final OrderRepository orderRepo;
    private final Processor processor;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo, Processor processor) {
        this.orderRepo = orderRepo;
        this.processor = processor;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder() {
        Order order = Order.createOrder();
        orderRepo.save(order);
        processor.output()
                .send(MessageBuilder.withPayload(new OrderCreated(order.getId(), order.getCreateBy()))
                        .build());
    }
}
