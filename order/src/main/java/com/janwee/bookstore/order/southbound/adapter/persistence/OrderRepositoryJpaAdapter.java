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

    @Override
    public Page<Order> ordersOf(Pageable pageable) {
        return jpaRepo.findAll(pageable)
                .map(OrderPOAssembler::toDomain);
    }

    @Override
    public Optional<Order> orderOf(Long id) {
        return jpaRepo.findById(id)
                .map(OrderPOAssembler::toDomain);
    }

    @Override
    public void add(Order order) {
        Assert.notNull(order, "Order is required");
        Assert.isNull(order.id(), "New order must not already have an ID");
        OrderPO saved = jpaRepo.save(OrderPOAssembler.toPO(order));
        order.assignId(saved.getId());
    }

    @Override
    public void update(Order order) {
        Assert.notNull(order, "Order is required");
        Long id = order.id();
        Assert.notNull(id, "Existing order ID is required for update");
        Assert.isTrue(jpaRepo.existsById(id), "Existing order must be present before update");
        jpaRepo.save(OrderPOAssembler.toPO(order));
    }
}
