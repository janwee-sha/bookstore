package com.janwee.bookstore.order.core.southbound.port;

import com.janwee.bookstore.order.core.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
