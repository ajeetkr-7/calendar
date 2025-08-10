package com.accoladehq.calendar.application.common.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TimeIntervalDTO(
        @NotNull
        LocalTime startTime,
        @NotNull
        LocalTime endTime
) {

}
