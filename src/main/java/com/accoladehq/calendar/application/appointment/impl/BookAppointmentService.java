package com.accoladehq.calendar.application.appointment.impl;

import com.accoladehq.calendar.application.appointment.BookAppointmentUseCase;
import com.accoladehq.calendar.application.common.annotations.UseCase;
import com.accoladehq.calendar.application.common.dto.AppointmentDTO;
import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.dto.BookAppointmentResponse;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.repository.AppointmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class BookAppointmentService implements BookAppointmentUseCase {

    private final AppointmentRepository appointmentRepository;

    @Override
    public BookAppointmentResponse execute(@Valid BookAppointmentRequest request) throws SlotNotAvailableException {
        // create a new appointment from the request
        var appointment = Appointment.builder()
                .userId(request.ownerId())
                .inviteeName(request.inviteeName())
                .inviteeEmail(request.inviteeEmail())
                .appointmentDate(request.appointmentDate())
                .duration(new TimeInterval(request.duration().startTime(), request.duration().endTime()))
                .build();
        // validate if the slot is available
        boolean appointmentExists = appointmentRepository.isTimeSlotAvailable(
                appointment.getUserId(),
                appointment.getAppointmentDate(),
                appointment.getDuration().getStartTime(),
                appointment.getDuration().getEndTime()
        );
        if (!appointmentExists) {
            throw new SlotNotAvailableException("The selected time slot is not available.");
        }
        // and save it to the repository
        appointment = appointmentRepository.save(appointment);

        AppointmentDTO dto = new AppointmentDTO(
                appointment.getId().toString(),
                appointment.getUserId().toString(),
                appointment.getInviteeName(),
                appointment.getInviteeEmail(),
                appointment.getAppointmentDate(),
                new TimeIntervalDTO(
                        appointment.getDuration().getStartTime(),
                        appointment.getDuration().getEndTime()
                )
        );
        return new BookAppointmentResponse(dto);
    }

}
