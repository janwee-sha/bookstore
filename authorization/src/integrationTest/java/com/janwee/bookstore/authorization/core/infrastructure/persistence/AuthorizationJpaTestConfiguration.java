package com.janwee.bookstore.authorization.core.infrastructure.persistence;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan("com.janwee.bookstore.authorization.core.infrastructure.persistence")
@EnableJpaRepositories("com.janwee.bookstore.authorization.core.infrastructure.persistence.jpa")
class AuthorizationJpaTestConfiguration {
}
