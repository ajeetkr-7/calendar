package com.accoladehq.calendar.infrastructure.persistence;

import com.accoladehq.calendar.domain.model.Appointment;
import com.accoladehq.calendar.domain.repository.AppointmentRepository;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final JpaAppointmentRepository repository;

    @Override
    public List<Appointment> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public List<Appointment> findAllUpcomingByUserId(UUID userId) {
        return repository.findAllUpcomingByUserId(userId);
    }

    @Override
    public List<Appointment> findAllByUserIdAndAppointmentDate(UUID userId, LocalDate appointmentDate) {
        return repository.findAllByUserIdAndAppointmentDate(userId, appointmentDate);
    }

    @Override
    public boolean isTimeSlotAvailable(UUID userId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        return repository.isSlotAvailable(userId, appointmentDate, startTime, endTime);
    }

    @Override
    public Appointment save(Appointment appointment) {
        return repository.save(appointment);
    }
}
