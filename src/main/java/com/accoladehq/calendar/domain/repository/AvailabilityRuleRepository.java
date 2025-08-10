package com.accoladehq.calendar.domain.repository;

import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AvailabilityRuleRepository {

    List<AvailabilityRule> findAllByUserId(UUID userId);

    Set<TimeInterval> findAllIntervalsByUserIdAndDate(UUID userId, LocalDate availabilityDate);
}
