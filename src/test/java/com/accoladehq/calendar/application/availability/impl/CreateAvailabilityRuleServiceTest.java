package com.accoladehq.calendar.application.availability.impl;

import com.accoladehq.calendar.application.common.dto.CreateAvailabilityRuleRequest;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.AvailabilityRule;
import com.accoladehq.calendar.domain.model.TimeInterval;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.domain.repository.AvailabilityRuleRepository;
import com.accoladehq.calendar.domain.repository.UserRepository;

@SpringBootTest
class CreateAvailabilityRuleServiceTest {

    @Autowired
    private CreateAvailabilityRuleService service;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AvailabilityRuleRepository availabilityRuleRepository;

    @Test
    void testExecuteCreatesAvailabilityRuleForValidUser() throws UserNotFoundException {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.MONDAY,
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        when(userRepository.existsById(userId)).thenReturn(true);
        AvailabilityRule savedRule = AvailabilityRule.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .weekday(request.weekday())
                .duration(new TimeInterval(request.interval().startTime(), request.interval().endTime()))
                .build();
        when(availabilityRuleRepository.save(ArgumentMatchers.any(AvailabilityRule.class))).thenReturn(savedRule);
        assertDoesNotThrow(() -> service.execute(userId, request));
    }

    @Test
    void testExecuteThrowsUserNotFoundException() {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.MONDAY,
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        when(userRepository.existsById(userId)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> service.execute(userId, request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForNullUserId() {
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.MONDAY,
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        assertThrows(ConstraintViolationException.class, () -> service.execute(null, request));
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForInvalidRequest() {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                null,
                null
        );
        when(userRepository.existsById(userId)).thenReturn(true);
        assertThrows(ConstraintViolationException.class, () -> service.execute(userId, request));
    }

}
