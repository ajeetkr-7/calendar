package com.accoladehq.calendar.application.common.dto;

import java.util.List;

public record ListUpcomingAppointmentsResponse(
        List<AppointmentDTO> appointments
) {
}
