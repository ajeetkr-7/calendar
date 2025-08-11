package com.accoladehq.calendar.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accoladehq.calendar.application.appointment.BookAppointmentUseCase;
import com.accoladehq.calendar.application.appointment.ListUpcomingAppointmentUseCase;
import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.exceptions.InvalidRequestDataException;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.presentation.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment APIs", description = "APIs for listing and booking appointments")
public class AppointmentController {

    private final BookAppointmentUseCase bookAppointmentUseCase;
    private final ListUpcomingAppointmentUseCase listUpcomingAppointmentUseCase;

    @GetMapping
    @Operation(
        summary = "List Upcoming Appointments",
        description = "Lists all upcoming appointments for the specified user.",
        tags = { "Appointment" }
    )
    public ResponseEntity<?> listUpcomingAppointments(@Valid @RequestHeader("uid") UUID userId) {
        ResponseEntity<?> response;
        try {
            response = ResponseEntity.ok(listUpcomingAppointmentUseCase.execute(userId));
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(ErrorResponse.from(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping
    @Operation(
        summary = "Book Appointment",
        description = "Books an appointment for the specified slot and user.",
        tags = { "Appointment" }
    )
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody BookAppointmentRequest request) {
        ResponseEntity<?> response;
        try {
            response = ResponseEntity.status(HttpStatus.CREATED).body(bookAppointmentUseCase.execute(request));
        } catch (UserNotFoundException | SlotNotAvailableException | InvalidRequestDataException e) {
            response = new ResponseEntity<>(ErrorResponse.from(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(ErrorResponse.from(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
