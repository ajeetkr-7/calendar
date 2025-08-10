package com.accoladehq.calendar.application.availability.impl;

import com.accoladehq.calendar.application.availability.GetAvailableSlotsUseCase;
import com.accoladehq.calendar.application.common.annotations.UseCase;
import com.accoladehq.calendar.application.common.dto.GetAvailableSlotResponse;
import com.accoladehq.calendar.application.common.dto.ScheduleOwnerDTO;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.domain.repository.AvailabilityRuleRepository;
import com.accoladehq.calendar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetAvailableSlotsService implements GetAvailableSlotsUseCase {

    private final AvailabilityRuleRepository availabilityRuleRepository;
    private final UserRepository userRepository;

    @Override
    public GetAvailableSlotResponse execute(UUID userId, LocalDate date) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Set<TimeInterval> slots = availabilityRuleRepository.findAllIntervalsByUserIdAndDate(userId, date);
        return new GetAvailableSlotResponse(
                new ScheduleOwnerDTO(user.getId().toString(), user.getName(), user.getEmail()),
                date,
                slots.stream()
                        .map(slot -> new TimeIntervalDTO(slot.getStartTime(), slot.getEndTime())).collect(Collectors.toSet())
        );
    }
}
