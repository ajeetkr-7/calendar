package com.accoladehq.calendar.domain.model;

import java.util.UUID;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class AvailabilityRuleTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    AvailabilityRule buildValidRule() {
        return AvailabilityRule.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .weekday(DayOfWeek.MONDAY)
                .duration(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .build();
    }


    @Test
    void validRuleShouldHaveNoViolations() {
        AvailabilityRule rule = buildValidRule();
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullUserIdShouldCauseViolation() {
        AvailabilityRule rule = buildValidRule();
        rule.setUserId(null);
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    void nullWeekdayShouldCauseViolation() {
        AvailabilityRule rule = buildValidRule();
        rule.setWeekday(null);
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("weekday")));
    }

    @Test
    void nullDurationShouldCauseViolation() {
        AvailabilityRule rule = buildValidRule();
        rule.setDuration(null);
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void invalidTimeIntervalShouldCauseViolation() {
        AvailabilityRule rule = buildValidRule();
        TimeInterval interval = new TimeInterval();
        interval.setStartTime(LocalTime.of(9, 30));
        interval.setEndTime(LocalTime.of(10, 30));
        rule.setDuration(interval);
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void durationLessThanMinShouldCauseViolation() {
        AvailabilityRule rule = buildValidRule();
        TimeInterval interval = new TimeInterval();
        interval.setStartTime(LocalTime.of(9, 0));
        interval.setEndTime(LocalTime.of(9, 30));
        rule.setDuration(interval);
        Set<ConstraintViolation<AvailabilityRule>> violations = validator.validate(rule);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void testEqualsAndHashCode() {
        AvailabilityRule r1 = buildValidRule();
        AvailabilityRule r2 = buildValidRule();
        r2.setId(r1.getId());
        r2.setUserId(r1.getUserId());
        r2.setWeekday(r1.getWeekday());
        r2.setDuration(r1.getDuration());
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNotEquals() {
        AvailabilityRule r1 = buildValidRule();
        AvailabilityRule r2 = buildValidRule();
        r2.setWeekday(DayOfWeek.FRIDAY);
        assertNotEquals(r1, r2);
    }
}
