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

import static org.junit.jupiter.api.Assertions.*;

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
                .duration(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(10, 0)))
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
    void nullDurationShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        appointment.setDuration(null);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }


    /// test case for startTime and endTime being whole hours
    @Test
    void nonHourAppointmentTimeShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        TimeInterval interval = new TimeInterval();
        // minutes are not zero here
        interval.setStartTime(LocalTime.of(9, 30));
        interval.setEndTime(LocalTime.of(10, 30));
        appointment.setDuration(interval);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    /// test case for startTime after endTime combination
    @Test
    void incorrectDurationShouldCauseViolation() {
        Appointment appointment = buildValidAppointment();
        TimeInterval interval = new TimeInterval();
        // interval is not 60 minutes
        interval.setStartTime(LocalTime.of(9, 0));
        interval.setEndTime(LocalTime.of(9, 30));
        appointment.setDuration(interval);
        Set<ConstraintViolation<Appointment>> violations = validator.validate(appointment);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void testEqualsAndHashCode() {
        Appointment a1 = buildValidAppointment();
        Appointment a2 = buildValidAppointment();
        a2.setId(a1.getId());
        a2.setUserId(a1.getUserId());
        a2.setInviteeName(a1.getInviteeName());
        a2.setInviteeEmail(a1.getInviteeEmail());
        a2.setAppointmentDate(a1.getAppointmentDate());
        a2.setDuration(a1.getDuration());
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testNotEquals() {
        Appointment a1 = buildValidAppointment();
        Appointment a2 = buildValidAppointment();
        a2.setId(UUID.randomUUID()); // Different ID
        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }
}
