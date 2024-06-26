package com.janwee.bookstore.order.southbound.port;

import com.janwee.bookstore.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
