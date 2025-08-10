package com.accoladehq.calendar.domain.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppointmentTest {

    private Validator validator;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    Appointment buildValidAppointment() {
        return Appointment.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .inviteeName("Jane Doe")
                .inviteeEmail("jane.doe@example.com")
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .build();
    }

    @Test
    void validAppointmentShouldHaveNoViolations() {
        Appointment appointment = buildValidAppointment();
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullUserIdShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setUserId(null);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    void blankInviteeNameShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setInviteeName("");
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("inviteeName")));
    }

    @Test
    void invalidInviteeEmailShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setInviteeEmail("not-an-email");
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("inviteeEmail")));
    }

    @Test
    void nullAppointmentDateShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setAppointmentDate(null);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("appointmentDate")));
    }

    @Test
    void nullStartTimeShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setStartTime(null);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("startTime")));
    }

    @Test
    void nullEndTimeShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setEndTime(null);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endTime")));
    }

}
