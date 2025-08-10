package com.accoladehq.calendar.application.appointment.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.accoladehq.calendar.application.common.dto.AppointmentDTO;
import com.accoladehq.calendar.application.common.dto.ListUpcomingAppointmentsResponse;
import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.repository.AppointmentRepository;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class ListUpcomingAppointmentServiceTest {

    @MockitoBean
    private AppointmentRepository repository;

    @Autowired
    private ListUpcomingAppointmentService service;

    Appointment buildValidAppointment(UUID userId) {
        return Appointment.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .inviteeName("John Doe")
                .inviteeEmail("john.doe@example.com")
                .duration(new TimeInterval(LocalTime.of(10, 0), LocalTime.of(11, 0)))
                .build();
    }

    @Test
    void testExecuteReturnsMappedDTOs() {
        UUID userId = UUID.randomUUID();
        Appointment appointment = buildValidAppointment(userId);
        when(repository.findAllUpcomingByUserId(userId)).thenReturn(List.of(appointment));

        ListUpcomingAppointmentsResponse response = service.execute(userId);
        assertNotNull(response);
        assertEquals(1, response.appointments().size());
        AppointmentDTO dto = response.appointments().get(0);
        assertEquals(appointment.getId().toString(), dto.id());
        assertEquals(appointment.getUserId().toString(), dto.ownerId());
        assertEquals(appointment.getInviteeName(), dto.inviteeName());
        assertEquals(appointment.getInviteeEmail(), dto.inviteeEmail());
        assertEquals(appointment.getAppointmentDate(), dto.appointmentDate());
        assertEquals(appointment.getDuration().getStartTime(), dto.duration().startTime());
        assertEquals(appointment.getDuration().getEndTime(), dto.duration().endTime());
    }

    @Test
    void testExecuteReturnsEmptyList() {
        UUID userId = UUID.randomUUID();
        when(repository.findAllUpcomingByUserId(userId)).thenReturn(List.of());
        ListUpcomingAppointmentsResponse response = service.execute(userId);
        assertNotNull(response);
        assertTrue(response.appointments().isEmpty());
    }

    @Test
    void testExecuteThrowsExceptionForNullUUID() {
        assertThrows(ConstraintViolationException.class, () -> service.execute(null));
    }

}
