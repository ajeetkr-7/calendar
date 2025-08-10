package com.accoladehq.calendar.domain.model;


import java.time.DayOfWeek;
import java.util.UUID;

import com.accoladehq.calendar.application.common.annotations.validation.ValidTimeInterval;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startTime", column = @Column(name = "start_time", nullable = false)),
            @AttributeOverride(name = "endTime", column = @Column(name = "end_time", nullable = false))
    })
    @ValidTimeInterval(min = 60, message = "Duration must be 60 minutes")
    private TimeInterval duration;
}
