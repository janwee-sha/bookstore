package com.janwee.bookstore.authorization.core.infrastructure.persistence.jpa;

import com.janwee.bookstore.authorization.core.infrastructure.persistence.SecurityBasedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityBasedUserJpaRepository extends JpaRepository<SecurityBasedUser, Long> {
    Optional<SecurityBasedUser> findByEmail(String email);
}
