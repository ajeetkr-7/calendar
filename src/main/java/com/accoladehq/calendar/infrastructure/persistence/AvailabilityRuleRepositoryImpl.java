package com.accoladehq.calendar.infrastructure.persistence;

import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.repository.AvailabilityRuleRepository;
import com.accoladehq.calendar.infrastructure.mapper.TimeIntervalMapper;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaAvailabilityRuleRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Validated
public class AvailabilityRuleRepositoryImpl implements AvailabilityRuleRepository {

    private final JpaAvailabilityRuleRepository repository;

    @Override
    public AvailabilityRule save(@NotNull @Valid AvailabilityRule availabilityRule) {
        return repository.save(availabilityRule);
    }

    @Override
    public List<AvailabilityRule> findAllByUserId(@NotNull UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public Set<TimeInterval> findAllIntervalsByUserIdAndDate(@NotNull UUID userId, @NotNull LocalDate availabilityDate) {
        return repository.findAllIntervalsByUserIdAndDate(userId, availabilityDate).stream().map(
                TimeIntervalMapper::from
        ).collect(Collectors.toSet());
    }
}
