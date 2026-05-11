package com.janwee.bookstore.authorization.core.southbound.adapter;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan("com.janwee.bookstore.authorization.core.southbound.adapter")
@EnableJpaRepositories("com.janwee.bookstore.authorization.core.southbound.adapter.jpa")
class AuthorizationJpaTestConfiguration {
}
