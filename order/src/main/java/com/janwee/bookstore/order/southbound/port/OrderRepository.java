package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository {
    Page<Order> ordersOf(Pageable pageable);

    Optional<Order> orderOf(Long id);

    void save(Order order);
}
