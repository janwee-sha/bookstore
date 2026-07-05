package com.janwee.bookstore.order.southbound.adapter.persistence;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.southbound.port.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryJpaAdapter implements OrderRepository {
    private final OrderPOJpaRepository jpaRepo;
    private final TicketPOJpaRepository ticketJpaRepo;

    @Override
    public Page<Order> ordersOf(Pageable pageable) {
        return jpaRepo.findAll(pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Order> orderOf(Long id) {
        return jpaRepo.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void save(Order order) {
        Assert.notNull(order, "Order is required");
        OrderPO saved = jpaRepo.save(OrderPOAssembler.toPO(order));
        order.assignId(saved.getId());
        if (order.ticket() != null) {
            TicketPO ticketPO = ticketJpaRepo.save(TicketPOAssembler.toPO(order.ticket()));
            order.ticket().assignId(ticketPO.getId());
        }
    }

    private Order toDomain(OrderPO po) {
        Order order = OrderPOAssembler.toDomain(po);
        ticketJpaRepo.findByOrderId(order.id())
                .map(TicketPOAssembler::toDomain)
                .ifPresent(order::assignTicket);
        return order;
    }
}
