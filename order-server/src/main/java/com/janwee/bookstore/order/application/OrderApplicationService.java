package com.janwee.bookstore.order.application;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderApplicationService {
    private final OrderRepository orderRepo;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createOrder() {
        Order order = Order.createOrder();
        orderRepo.save(order);

    }
}
