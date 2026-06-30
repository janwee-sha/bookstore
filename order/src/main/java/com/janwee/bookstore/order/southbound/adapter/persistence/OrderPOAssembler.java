package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.State;

public class OrderPOAssembler {
    public static Order toDomain(OrderPO po) {
        if (po == null) {
            return null;
        }

        return new Order(
                po.getId(),
                po.getBookId(),
                po.getAmount(),
                po.getCreatedAt(),
                State.valueOf(po.getState())
        );
    }

    public static OrderPO toPO(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderPO(
                order.id(),
                order.bookId(),
                order.amount(),
                order.createdAt(),
                order.state().name()
        );
    }
}
