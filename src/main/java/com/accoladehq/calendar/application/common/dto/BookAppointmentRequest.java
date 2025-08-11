package com.accoladehq.calendar.application.common.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.accoladehq.calendar.application.common.annotations.validation.ValidTimeInterval;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookAppointmentRequest(
        @NotNull(message = "Owner ID cannot be null")
        UUID ownerId,
        @NotBlank(message = "Invitee name cannot be blank")
        String inviteeName,
        @NotNull(message = "Invitee email cannot be null")
        @Email(message = "Invitee email should be valid")
        String inviteeEmail,
        @NotNull(message = "Appointment date cannot be null")
        @FutureOrPresent(message = "Appointment date must be today or in the future")
        LocalDate appointmentDate,
        @NotNull(message = "Duration cannot be null")
        @ValidTimeInterval(diff = 60, message = "Duration must be 60 minutes")
        TimeIntervalDTO duration
) {

}