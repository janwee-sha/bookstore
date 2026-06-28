package com.janwee.bookstore.order.southbound.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPOJpaRepository extends JpaRepository<OrderPO, Long> {
}
