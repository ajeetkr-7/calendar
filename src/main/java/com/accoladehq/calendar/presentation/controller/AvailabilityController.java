package com.accoladehq.calendar.presentation.controller;

import java.time.LocalDate;
import java.util.UUID;

import com.accoladehq.calendar.application.availability.CreateAvailabilityRuleUseCase;
import com.accoladehq.calendar.application.availability.GetAvailableSlotsUseCase;
import com.accoladehq.calendar.application.common.dto.CreateAvailabilityRuleRequest;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.presentation.dto.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final GetAvailableSlotsUseCase getAvailableSlotsUseCase;
    private final CreateAvailabilityRuleUseCase createAvailabilityRuleUseCase;

    @GetMapping
    public ResponseEntity<?> getAvailableSlots(
            @Valid @RequestHeader("uid") UUID userId,
            @Valid @RequestParam LocalDate date) {

        ResponseEntity<?> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(getAvailableSlotsUseCase.execute(userId, date));
        } catch (UserNotFoundException e) {
            responseEntity = new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<?> createAvailabilityRule(
            @Valid @RequestHeader("uid") UUID userId,
            @Valid @RequestBody CreateAvailabilityRuleRequest request) {
        ResponseEntity<?> responseEntity;
        try {
            createAvailabilityRuleUseCase.execute(userId, request);
            responseEntity = ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            responseEntity = new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

}
