package com.accoladehq.calendar.application.appointment;

import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.dto.BookAppointmentResponse;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import jakarta.validation.Valid;

public interface BookAppointmentUseCase {

    BookAppointmentResponse execute(@Valid BookAppointmentRequest request) throws SlotNotAvailableException;

}
