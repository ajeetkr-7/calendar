package com.accoladehq.calendar.domain.model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.accoladehq.calendar.domain.validation.ValidTimeInterval;
import jakarta.persistence.*;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startTime", column = @Column(name = "start_time", nullable = false)),
            @AttributeOverride(name = "endTime", column = @Column(name = "end_time", nullable = false))
    })
    @ValidTimeInterval(diff = 60, message = "Duration must be 60 minutes")
    private TimeInterval duration;

}
