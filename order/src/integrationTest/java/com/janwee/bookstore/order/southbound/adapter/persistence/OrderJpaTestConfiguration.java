package com.janwee.bookstore.order.southbound.adapter.persistence;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan("com.janwee.bookstore.order.southbound.adapter.persistence")
@EnableJpaRepositories("com.janwee.bookstore.order.southbound.adapter.persistence")
@Import({OrderRepositoryJpaAdapter.class, TicketRepositoryJpaAdapter.class})
class OrderJpaTestConfiguration {
}
