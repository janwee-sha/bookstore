package com.janwee.bookstore.authorization.southbound.adapter.jpa;

import com.janwee.bookstore.authorization.southbound.adapter.SpringSecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringSecurityUserJpaRepository extends JpaRepository<SpringSecurityUser, Long> {
    Optional<SpringSecurityUser> findByEmail(String email);
}
