package com.accoladehq.calendar.application.appointment.impl;

import java.util.List;
import java.util.UUID;

import com.accoladehq.calendar.application.appointment.ListUpcomingAppointmentUseCase;
import com.accoladehq.calendar.application.common.annotations.UseCase;
import com.accoladehq.calendar.application.common.dto.AppointmentDTO;
import com.accoladehq.calendar.application.common.dto.ListUpcomingAppointmentsResponse;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.domain.repository.AppointmentRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ListUpcomingAppointmentService implements ListUpcomingAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;

    @Override
    public ListUpcomingAppointmentsResponse execute(@NotNull UUID ownerId) {
        var appointments = appointmentRepository.findAllUpcomingByUserId(ownerId);

        List<AppointmentDTO> appointmentDTOList = appointments.stream()
                .map(appointment -> new AppointmentDTO(
                appointment.getId().toString(),
                appointment.getUserId().toString(),
                appointment.getInviteeName(),
                appointment.getInviteeEmail(),
                appointment.getAppointmentDate(),
                new TimeIntervalDTO(appointment.getDuration().getStartTime(),
                        appointment.getDuration().getEndTime()))
                ).toList();
        return new ListUpcomingAppointmentsResponse(appointmentDTOList);
    }

}
