package com.accoladehq.calendar.infrastructure.persistence;

import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.domain.repository.UserRepository;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaUserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Validated
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaRepository;

    @Override
    public Optional<User> findById(@NotNull UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public User save(@NotNull @Valid User user) {
        return jpaRepository.save(user);
    }

    @Override
    public boolean existsById(@NotNull @Valid UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(@Email String email) {
        return jpaRepository.existsByEmail(email);
    }
}
