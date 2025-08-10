package com.accoladehq.calendar.domain.model;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserTest {

    private Validator validator;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    User buildValidUser() {
        UUID userId = UUID.randomUUID();
        return User.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void validUserShouldHaveNoViolations() {
        User user = buildValidUser();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankNameShouldCauseViolation() {
        User user = buildValidUser();
        user.setName("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void nullNameShouldCauseViolation() {
        User user = buildValidUser();
        user.setName(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void blankEmailShouldCauseViolation() {
        User user = buildValidUser();
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void invalidEmailShouldCauseViolation() {
        User user = buildValidUser();
        user.setEmail("not-an-email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void nullEmailShouldCauseViolation() {
        User user = buildValidUser();
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = buildValidUser();
        User u2 = User.builder()
                .id(u1.getId())
                .name(u1.getName())
                .email(u1.getEmail())
                .build();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testNotEquals() {
        User u1 = buildValidUser();
        User u2 = buildValidUser();
        u2.setEmail("other@example.com");
        assertNotEquals(u1, u2);
    }

}