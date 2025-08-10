package com.accoladehq.calendar.application.appointment.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.dto.BookAppointmentResponse;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.repository.AppointmentRepository;

@SpringBootTest
class BookAppointmentServiceTest {

    @Autowired
    private BookAppointmentService service;

    @MockitoBean
    private AppointmentRepository repository;

    BookAppointmentRequest buildValidRequest() {
        return new BookAppointmentRequest(
                UUID.randomUUID(),
                "John Doe",
                "john.doe@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
    }

    @Test
    void testExecuteReturnsResponseWhenSlotAvailable() throws SlotNotAvailableException {
        BookAppointmentRequest request = buildValidRequest();

        when(repository.isTimeSlotAvailable(
                request.ownerId(),
                request.appointmentDate(),
                request.duration().startTime(),
                request.duration().endTime()
        )).thenReturn(true);

        Appointment savedAppointment = Appointment.builder()
                .id(UUID.randomUUID())
                .userId(request.ownerId())
                .inviteeName(request.inviteeName())
                .inviteeEmail(request.inviteeEmail())
                .appointmentDate(request.appointmentDate())
                .duration(new TimeInterval(request.duration().startTime(), request.duration().endTime()))
                .build();

        when(repository.save(org.mockito.ArgumentMatchers.any(Appointment.class)))
                .thenReturn(savedAppointment);

        BookAppointmentResponse response = service.execute(request);

        assertNotNull(response);
        assertNotNull(response.appointment());
        assertEquals(savedAppointment.getId().toString(), response.appointment().id());
        assertEquals(savedAppointment.getUserId().toString(), response.appointment().ownerId());
        assertEquals(savedAppointment.getInviteeName(), response.appointment().inviteeName());
        assertEquals(savedAppointment.getInviteeEmail(), response.appointment().inviteeEmail());
        assertEquals(savedAppointment.getAppointmentDate(), response.appointment().appointmentDate());
        assertEquals(savedAppointment.getDuration().getStartTime(), response.appointment().duration().startTime());
        assertEquals(savedAppointment.getDuration().getEndTime(), response.appointment().duration().endTime());
    }

    @Test
    void testExecuteThrowsSlotNotAvailableException() {
        BookAppointmentRequest request = buildValidRequest();

        when(repository.isTimeSlotAvailable(
                request.ownerId(),
                request.appointmentDate(),
                request.duration().startTime(),
                request.duration().endTime()
        )).thenReturn(false);

        assertThrows(SlotNotAvailableException.class, () -> service.execute(request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForNullOwnerId() {
        BookAppointmentRequest request = new BookAppointmentRequest(
                null,
                "John Doe",
                "john.doe@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForInvalidEmail() {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(),
                "John Doe",
                "invalid-email",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForPastDate() {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(),
                "John Doe",
                "john.doe@example.com",
                LocalDate.now().minusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForNullDuration() {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(),
                "John Doe",
                "john.doe@example.com",
                LocalDate.now().plusDays(1),
                null
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForNullInviteeName() {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(),
                null,
                "john.doe@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(request));
    }

}