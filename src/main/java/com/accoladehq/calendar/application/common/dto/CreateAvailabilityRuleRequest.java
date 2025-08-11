package com.accoladehq.calendar.application.common.dto;

import com.accoladehq.calendar.application.common.annotations.validation.ValidTimeInterval;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;

public record CreateAvailabilityRuleRequest(
        @NotNull(message = "weekday must not be null")
        DayOfWeek weekday,
        @ValidTimeInterval(message = "interval must be valid and non-empty")
        TimeIntervalDTO interval
        ) {

}
