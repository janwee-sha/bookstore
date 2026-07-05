package com.janwee.bookstore.order.southbound.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketPOJpaRepository extends JpaRepository<TicketPO, Long> {
    Optional<TicketPO> findByOrderId(Long orderId);
}
