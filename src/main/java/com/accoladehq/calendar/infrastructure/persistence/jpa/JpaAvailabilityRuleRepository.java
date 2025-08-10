package com.accoladehq.calendar.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.infrastructure.persistence.projections.TimeIntervalProjection;

public interface JpaAvailabilityRuleRepository extends JpaRepository<AvailabilityRule, UUID> {

    List<AvailabilityRule> findAllByUserId(UUID userId);


    @Query(value = """
            WITH user_availability AS (
                SELECT
                    CAST(DATEADD('HOUR', n.n, ar.start_time) AS TIME) AS slot_start,
                    CAST(DATEADD('HOUR', n.n + 1, ar.start_time) AS TIME) AS slot_end
                FROM availability_rule ar
                JOIN (
                    VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8)
                ) AS n(n)
                ON DATEADD('HOUR', n.n, ar.start_time) < ar.end_time
                WHERE ar.user_id = :userId
                AND TRIM(ar.weekday) = UPPER(TO_CHAR(CAST(:availabilityDate AS DATE), 'FMDay'))
            ),
            booked_slots AS (
                SELECT
                    CAST(DATEADD('HOUR', n.n, a.start_time) AS TIME) AS slot_start,
                    CAST(DATEADD('HOUR', n.n + 1, a.start_time) AS TIME) AS slot_end
                FROM appointment a
                JOIN (
                    VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8)
                ) AS n(n)
                ON DATEADD('HOUR', n.n, a.start_time) < a.end_time
                WHERE a.user_id = :userId
                AND a.appointment_date = CAST(:availabilityDate AS DATE)
            )
            SELECT
                CAST(ua.slot_start as TIME) as "startTime",
                CAST(ua.slot_end as TIME) as "endTime"
            FROM user_availability ua
            LEFT JOIN booked_slots bs
                ON ua.slot_start = bs.slot_start
            WHERE bs.slot_start IS NULL
            AND (
                    CAST(:availabilityDate AS DATE) > CURRENT_DATE
                    OR ua.slot_start > CURRENT_TIME
                )
            ORDER BY ua.slot_start;
            """, nativeQuery = true)
    Set<TimeIntervalProjection> findAllIntervalsByUserIdAndDate(UUID userId, LocalDate availabilityDate);

}
