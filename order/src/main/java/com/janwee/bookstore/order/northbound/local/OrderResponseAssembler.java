package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.northbound.message.OrderResponse;

public class OrderResponseAssembler {
    public static OrderResponse from(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponse response = new OrderResponse();
        response.setId(order.id());
        response.setBookId(order.bookId());
        response.setAmount(order.amount());
        response.setCreatedAt(order.createdAt());
        response.setState(order.state());
        return response;
    }
}
