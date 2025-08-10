package com.accoladehq.calendar.domain.model;


import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Owner user ID cannot be null")
    private UUID userId;

    @NotNull(message = "Weekday cannot be null")
    @Enumerated(EnumType.STRING)
    private DayOfWeek weekday;

    @NotNull(message = "Start time 'from' cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time 'to' cannot be null")
    private LocalTime endTime;

}
