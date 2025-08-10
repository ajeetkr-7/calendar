package com.accoladehq.calendar.domain.model;

import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval {

    @NotNull(message = "Start time 'from' cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time 'to' cannot be null")
    private LocalTime endTime;
}

