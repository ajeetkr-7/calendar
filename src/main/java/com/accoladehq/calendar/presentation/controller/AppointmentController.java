package com.accoladehq.calendar.presentation.controller;

import com.accoladehq.calendar.application.appointment.BookAppointmentUseCase;
import com.accoladehq.calendar.application.appointment.ListUpcomingAppointmentUseCase;
import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final BookAppointmentUseCase bookAppointmentUseCase;
    private final ListUpcomingAppointmentUseCase listUpcomingAppointmentUseCase;

    @GetMapping
    public ResponseEntity<?> listUpcomingAppointments(@Valid @RequestHeader("uid") UUID userId) {
        ResponseEntity<?> response;
        try {
            response = ResponseEntity.ok(listUpcomingAppointmentUseCase.execute(userId));
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody BookAppointmentRequest request) {
        ResponseEntity<?> response;
        try {
            response = ResponseEntity.status(HttpStatus.CREATED).body(bookAppointmentUseCase.execute(request));
        } catch (SlotNotAvailableException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
