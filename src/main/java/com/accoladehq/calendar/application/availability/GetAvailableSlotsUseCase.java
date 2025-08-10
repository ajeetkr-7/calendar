package com.accoladehq.calendar.application.availability;

import com.accoladehq.calendar.application.common.dto.GetAvailableSlotResponse;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public interface GetAvailableSlotsUseCase {

    public GetAvailableSlotResponse execute(@NotNull UUID userId, @NotNull LocalDate date);
}
