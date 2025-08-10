package com.accoladehq.calendar.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface AvailabilityRuleRepository {

    AvailabilityRule save(@NotNull @Valid AvailabilityRule availabilityRule);

    List<AvailabilityRule> findAllByUserId(@NotNull @Valid UUID userId);

    Set<TimeInterval> findAllIntervalsByUserIdAndDate(@NotNull @Valid UUID userId, @NotNull @Valid LocalDate availabilityDate);
}
