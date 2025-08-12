package com.accoladehq.calendar.infrastructure.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaAppointmentRepository;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaUserRepository;

@DataJpaTest
@Import(AppointmentRepositoryImpl.class)
class AppointmentRepositoryImplTest {

    @Autowired
    private AppointmentRepositoryImpl appointmentRepository;

    @Autowired
    private JpaAppointmentRepository jpaAppointmentRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        jpaAppointmentRepository.deleteAll();
        jpaAppointmentRepository.flush();
        jpaUserRepository.deleteAll();
        jpaUserRepository.flush();
    }

    Appointment buildValidAppointment(UUID userId) {
        return Appointment.builder()
                .id(null)
                .userId(userId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .inviteeName("John Doe")
                .inviteeEmail("john.doe@example.com")
                .duration(new TimeInterval(
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                )).build();
    }

    User buildValidUser() {
        return User.builder()
                .id(null)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
    }

    @Test
    void testSave() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        Appointment appointment = buildValidAppointment(user.getId());
        Appointment saved = appointmentRepository.save(appointment);

        assertNotNull(saved.getId());
        assertEquals(saved.getUserId(), appointment.getUserId());
        assertEquals(saved.getInviteeName(), appointment.getInviteeName());
        assertEquals(saved.getInviteeEmail(), appointment.getInviteeEmail());
        assertEquals(saved.getAppointmentDate(), appointment.getAppointmentDate());
        assertEquals(saved.getDuration(), appointment.getDuration());

    }

    @Test
    void testFindAllByUserId() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        Appointment appointment = buildValidAppointment(user.getId());
        appointment = appointmentRepository.save(appointment);

        var appointments = appointmentRepository.findAllByUserId(user.getId());
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
        assertEquals(appointment, appointments.get(0));

    }

    @Test
    void testFindAllUpcomingByUserId() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        Appointment pastAppointment = Appointment.builder()
                .id(null)
                .userId(user.getId())
                .appointmentDate(LocalDate.now().minusDays(1))
                .inviteeName("Past")
                .inviteeEmail("past@example.com")
                .duration(new TimeInterval(LocalTime.of(9, 0), LocalTime.of(10, 0)))
                .build();
        Appointment upcomingAppointment = buildValidAppointment(user.getId());

        jpaAppointmentRepository.save(pastAppointment);
        jpaAppointmentRepository.save(upcomingAppointment);

        var upcoming = appointmentRepository.findAllUpcomingByUserId(user.getId());
        assertFalse(upcoming.isEmpty());
        assertEquals(upcomingAppointment.getInviteeName(), upcoming.get(0).getInviteeName());
    }

    @Test
    void testFindAllByUserIdAndAppointmentDate() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        Appointment appointment = buildValidAppointment(user.getId());
        appointment = appointmentRepository.save(appointment);

        var appointments = appointmentRepository.findAllByUserIdAndAppointmentDate(
                user.getId(), appointment.getAppointmentDate());
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
        assertEquals(appointment, appointments.get(0));
    }

    @Test
    void testIsTimeSlotAvailable() {
        User user = buildValidUser();
        user = jpaUserRepository.save(user);

        Appointment appointment = buildValidAppointment(user.getId());
        appointmentRepository.save(appointment);

        // Slot already taken
        boolean available = appointmentRepository.isTimeSlotAvailable(
                user.getId(),
                appointment.getAppointmentDate(),
                appointment.getDuration().getStartTime(),
                appointment.getDuration().getEndTime()
        );
        assertFalse(available);

        // Different slot should be available
        boolean available2 = appointmentRepository.isTimeSlotAvailable(
                user.getId(),
                appointment.getAppointmentDate(),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0)
        );
        assertEquals(true, available2);
    }

}
