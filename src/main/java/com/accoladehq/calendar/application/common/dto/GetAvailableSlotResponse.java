package com.accoladehq.calendar.application.common.dto;

import java.time.LocalDate;
import java.util.Set;

public record GetAvailableSlotResponse(
        ScheduleOwnerDTO owner,
        LocalDate date,
        Set<TimeIntervalDTO> slots
) {

}
