package com.accoladehq.calendar.domain.model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Owner user ID cannot be null")
    private UUID userId;

    @NotBlank(message = "Invitee name cannot be blank")
    private String inviteeName;

    @Email(message = "Invitee email must be a valid email address")
    @NotBlank(message = "Invitee email cannot be blank")
    private String inviteeEmail;

    @NotNull(message = "Appointment date cannot be null")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time 'from' cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time 'to' cannot be null")
    private LocalTime endTime;

}
