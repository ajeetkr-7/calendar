package com.accoladehq.calendar.application.availability.impl;

import com.accoladehq.calendar.application.availability.CreateAvailabilityRuleUseCase;
import com.accoladehq.calendar.application.common.annotations.UseCase;
import com.accoladehq.calendar.application.common.dto.CreateAvailabilityRuleRequest;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.repository.AvailabilityRuleRepository;
import com.accoladehq.calendar.domain.repository.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CreateAvailabilityRuleService implements CreateAvailabilityRuleUseCase {

    private final AvailabilityRuleRepository availabilityRuleRepository;
    private final UserRepository userRepository;

    @Override
    public void execute(@NotNull UUID userId, @NotNull @Valid CreateAvailabilityRuleRequest request) throws UserNotFoundException {
        // Validate if the user exists
        boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        AvailabilityRule rule = AvailabilityRule.builder()
                .userId(userId)
                .weekday(request.weekday())
                .duration(new TimeInterval(request.interval().startTime(), request.interval().endTime()))
                .build();

        availabilityRuleRepository.save(rule);
    }
}
