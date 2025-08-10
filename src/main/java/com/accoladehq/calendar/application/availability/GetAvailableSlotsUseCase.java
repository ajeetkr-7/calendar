package com.accoladehq.calendar.application.availability;

import com.accoladehq.calendar.application.common.dto.GetAvailableSlotResponse;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public interface GetAvailableSlotsUseCase {

    GetAvailableSlotResponse execute(@NotNull UUID userId, @NotNull LocalDate date) throws UserNotFoundException;
}
