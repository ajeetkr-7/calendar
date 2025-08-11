package com.accoladehq.calendar.presentation.controller;

import com.accoladehq.calendar.application.availability.GetAvailableSlotsUseCase;
import com.accoladehq.calendar.application.availability.CreateAvailabilityRuleUseCase;
import com.accoladehq.calendar.application.common.dto.GetAvailableSlotResponse;
import com.accoladehq.calendar.application.common.dto.CreateAvailabilityRuleRequest;
import com.accoladehq.calendar.application.common.dto.ScheduleOwnerDTO;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
@ExtendWith(MockitoExtension.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GetAvailableSlotsUseCase getAvailableSlotsUseCase;

    @MockitoBean
    private CreateAvailabilityRuleUseCase createAvailabilityRuleUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAvailableSlots_success() throws Exception {
        UUID userId = UUID.randomUUID();
        ScheduleOwnerDTO owner = new ScheduleOwnerDTO(userId.toString(), "John Doe", "john.doe@example.com");
        LocalDate date = LocalDate.now().plusDays(1);
        GetAvailableSlotResponse response = new GetAvailableSlotResponse(owner, date,
                Set.of(new TimeIntervalDTO(LocalTime.of(9, 0), LocalTime.of(10, 0)),
                        new TimeIntervalDTO(LocalTime.of(11, 0), LocalTime.of(12, 0))));
        when(getAvailableSlotsUseCase.execute(any(UUID.class), any(LocalDate.class))).thenReturn(response);
        mockMvc.perform(get("/api/availability")
                .header("uid", userId)
                .param("date", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(getAvailableSlotsUseCase, times(1)).execute(userId, date);
    }

    @Test
    void getAvailableSlots_userNotFound_throws400() throws Exception {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);
        var exception = new UserNotFoundException("User not found");
        when(getAvailableSlotsUseCase.execute(any(UUID.class), any(LocalDate.class))).thenThrow(exception);
        mockMvc.perform(get("/api/availability")
                .header("uid", userId)
                .param("date", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(getAvailableSlotsUseCase, times(1)).execute(userId, date);
    }

    @Test
    void getAvailableSlots_genericException_throws500() throws Exception {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now().plusDays(1);
        var exception = new RuntimeException("Something went wrong");
        when(getAvailableSlotsUseCase.execute(any(UUID.class), any(LocalDate.class))).thenThrow(exception);
        mockMvc.perform(get("/api/availability")
                .header("uid", userId)
                .param("date", date.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(getAvailableSlotsUseCase, times(1)).execute(userId, date);
    }

    @Test
    void getAvailableSlots_invalidRequest_throws400() throws Exception {
        String userId = "invalid-uuid";
        String date = "invalid-date";
        mockMvc.perform(get("/api/availability")
                .header("uid", userId)
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.uid").value("Invalid value: " + userId + ". Expected type: UUID"));
    }

    @Test
    void createAvailabilityRule_success() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.MONDAY,
                new TimeIntervalDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))
        );
        mockMvc.perform(post("/api/availability")
                .header("uid", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
        verify(createAvailabilityRuleUseCase, times(1)).execute(userId, request);
    }

    @Test
    void createAvailabilityRule_userNotFound_throws400() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.TUESDAY,
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(12, 0))
        );
        var exception = new UserNotFoundException("User not found");
        doThrow(exception).when(createAvailabilityRuleUseCase).execute(any(UUID.class), any(CreateAvailabilityRuleRequest.class));
        mockMvc.perform(post("/api/availability")
                .header("uid", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(createAvailabilityRuleUseCase, times(1)).execute(userId, request);
    }

    @Test
    void createAvailabilityRule_genericException_throws500() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                DayOfWeek.WEDNESDAY,
                new TimeIntervalDTO(LocalTime.of(8, 0), LocalTime.of(9, 0))
        );
        var exception = new RuntimeException("Something went wrong");
        doThrow(exception).when(createAvailabilityRuleUseCase).execute(any(UUID.class), any(CreateAvailabilityRuleRequest.class));
        mockMvc.perform(post("/api/availability")
                .header("uid", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(createAvailabilityRuleUseCase, times(1)).execute(userId, request);
    }

    @Test
    void createAvailabilityRule_invalidRequest_throws400() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateAvailabilityRuleRequest request = new CreateAvailabilityRuleRequest(
                null,
                null
        );
        mockMvc.perform(post("/api/availability")
                .header("uid", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.weekday").value("weekday must not be null"))
                .andExpect(jsonPath("$.errors.interval").value("interval must be valid and non-empty"));
    }
}
