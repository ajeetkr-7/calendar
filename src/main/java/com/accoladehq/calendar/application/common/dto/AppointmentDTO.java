package com.accoladehq.calendar.application.common.dto;

import java.time.LocalDate;

public record AppointmentDTO(
        String id,
        String ownerId,
        String inviteeName,
        String inviteeEmail,
        LocalDate appointmentDate,
        TimeIntervalDTO duration
) {

}