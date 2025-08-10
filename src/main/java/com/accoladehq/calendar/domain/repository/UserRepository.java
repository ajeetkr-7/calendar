package com.accoladehq.calendar.domain.repository;

import com.accoladehq.calendar.domain.model.User;

import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);
    void save(User user);
    boolean existsByEmail(String email);
}
