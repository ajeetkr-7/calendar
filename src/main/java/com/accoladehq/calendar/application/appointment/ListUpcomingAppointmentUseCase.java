package com.accoladehq.calendar.application.appointment;

import java.util.UUID;


import com.accoladehq.calendar.application.common.dto.ListUpcomingAppointmentsResponse;
import jakarta.validation.constraints.NotNull;

public interface ListUpcomingAppointmentUseCase {

    ListUpcomingAppointmentsResponse execute(@NotNull UUID ownerId);

}
