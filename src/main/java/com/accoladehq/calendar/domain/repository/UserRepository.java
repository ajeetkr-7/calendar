package com.accoladehq.calendar.domain.repository;

import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(@NotNull  UUID id) throws UserNotFoundException;

    User save(@NotNull @Valid User user);

    boolean existsById(@NotNull UUID id);

    boolean existsByEmail(@Email String email);
}
