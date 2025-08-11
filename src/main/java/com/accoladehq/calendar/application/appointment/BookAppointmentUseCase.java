package com.accoladehq.calendar.application.appointment;

import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.dto.BookAppointmentResponse;
import com.accoladehq.calendar.application.common.exceptions.InvalidRequestDataException;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;

import jakarta.validation.Valid;

public interface BookAppointmentUseCase {

    BookAppointmentResponse execute(@Valid BookAppointmentRequest request) throws InvalidRequestDataException, UserNotFoundException, SlotNotAvailableException;

}
