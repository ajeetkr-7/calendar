package com.accoladehq.calendar.infrastructure.persistence;

import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaAppointmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AppointmentRepositoryImpl.class)
class AppointmentRepositoryImplTest {

    @Autowired
    private AppointmentRepositoryImpl appointmentRepository;

    @Autowired
    private JpaAppointmentRepository jpaAppointmentRepository;

    @Test
    void testSave() {
        Appointment appointment = buildValidAppointment();
        Appointment saved = appointmentRepository.save(appointment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(appointment.getUserId());
        assertThat(saved.getInviteeName()).isEqualTo(appointment.getInviteeName());
        assertThat(saved.getInviteeEmail()).isEqualTo(appointment.getInviteeEmail());
        assertThat(saved.getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
        assertThat(saved.getDuration()).isEqualTo(appointment.getDuration());

    }

    Appointment buildValidAppointment() {
        UUID userId = UUID.randomUUID();
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

}