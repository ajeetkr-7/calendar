package com.accoladehq.calendar.domain.repository;

import com.accoladehq.calendar.domain.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository {
    List<Appointment> findAllByUserId(UUID userId);
    List<Appointment> findAllByUserIdAndAppointmentDate(UUID userId, LocalDate appointmentDate);
    boolean isTimeSlotAvailable(UUID userId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime);
}
