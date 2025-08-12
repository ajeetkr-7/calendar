package com.accoladehq.calendar.infrastructure.persistence.jpa;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.accoladehq.calendar.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAllByUserId(UUID userId);

    @Query(
            "SELECT a FROM Appointment a "
            + "WHERE a.userId = :userId "
            + "AND a.appointmentDate >= CURRENT_DATE "
            + "AND (a.appointmentDate >= CURRENT_DATE OR a.duration.startTime > CURRENT_TIME) "
            + "ORDER BY a.appointmentDate, a.duration.startTime"
    )
    List<Appointment> findAllUpcomingByUserId(UUID userId);

    List<Appointment> findAllByUserIdAndAppointmentDate(UUID userId, LocalDate appointmentDate);

    @Query(
            "SELECT CASE WHEN COUNT(a) = 0 THEN true ELSE false END "
            + "FROM Appointment a "
            + "WHERE a.userId = :userId "
            + "AND a.appointmentDate = :appointmentDate "
            + "AND (a.duration.startTime = :startTime AND a.duration.endTime = :endTime )"
    )
    boolean isSlotAvailable(
            UUID userId,
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime
    );
}