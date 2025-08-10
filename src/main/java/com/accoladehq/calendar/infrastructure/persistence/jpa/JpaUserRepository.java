package com.accoladehq.calendar.infrastructure.persistence.jpa;

import com.accoladehq.calendar.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}
