package com.accoladehq.calendar.application.availability;

import com.accoladehq.calendar.application.common.dto.CreateAvailabilityRuleRequest;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface CreateAvailabilityRuleUseCase {

    public void execute(@NotNull UUID userId, @NotNull @Valid CreateAvailabilityRuleRequest request) throws UserNotFoundException;
}
