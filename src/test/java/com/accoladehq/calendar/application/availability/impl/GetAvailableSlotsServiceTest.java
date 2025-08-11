package com.accoladehq.calendar.application.availability.impl;

import com.accoladehq.calendar.application.common.dto.GetAvailableSlotResponse;
import com.accoladehq.calendar.application.common.dto.ScheduleOwnerDTO;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.domain.repository.AvailabilityRuleRepository;
import com.accoladehq.calendar.domain.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetAvailableSlotsServiceTest {

    @Autowired
    private GetAvailableSlotsService service;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AvailabilityRuleRepository availabilityRuleRepository;

    User buildValidUser(UUID id) {
        return User.builder()
                .id(id)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void testExecuteReturnsSlotsForValidUser() throws UserNotFoundException {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);
        User user = buildValidUser(userId);
        Set<TimeInterval> intervals = Set.of(new TimeInterval(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(availabilityRuleRepository.findAllIntervalsByUserIdAndDate(userId, date)).thenReturn(intervals);
        GetAvailableSlotResponse response = service.execute(userId, date);
        assertNotNull(response);
        assertEquals(userId.toString(), response.owner().id());
        assertEquals(date, response.date());
        assertEquals(1, response.slots().size());
        TimeIntervalDTO slot = response.slots().iterator().next();
        assertEquals(LocalTime.of(10, 0), slot.startTime());
        assertEquals(LocalTime.of(11, 0), slot.endTime());
    }

    @Test
    void testExecuteReturnsEmptySlots() throws UserNotFoundException {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);
        User user = buildValidUser(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(availabilityRuleRepository.findAllIntervalsByUserIdAndDate(userId, date)).thenReturn(Set.of());
        GetAvailableSlotResponse response = service.execute(userId, date);
        assertNotNull(response);
        assertTrue(response.slots().isEmpty());
    }

    @Test
    void testExecuteThrowsConstraintViolationExceptionForNullUserId() {
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(ConstraintViolationException.class, () -> service.execute(null, date));
    }

    @Test
    void testExecuteThrowsUserNotFoundException() throws UserNotFoundException {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.execute(userId, date));
    }
}
