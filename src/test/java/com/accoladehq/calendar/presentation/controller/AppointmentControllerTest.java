package com.accoladehq.calendar.presentation.controller;

import com.accoladehq.calendar.application.appointment.BookAppointmentUseCase;
import com.accoladehq.calendar.application.appointment.ListUpcomingAppointmentUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import com.accoladehq.calendar.application.common.dto.BookAppointmentRequest;
import com.accoladehq.calendar.application.common.dto.BookAppointmentResponse;
import com.accoladehq.calendar.application.common.dto.AppointmentDTO;
import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;
import com.accoladehq.calendar.application.common.dto.ListUpcomingAppointmentsResponse;
import com.accoladehq.calendar.application.common.exceptions.SlotNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AppointmentController.class)
@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookAppointmentUseCase bookAppointmentUseCase;

    @MockitoBean
    private ListUpcomingAppointmentUseCase listUpcomingAppointmentUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void bookAppointment_success() throws Exception {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(), "Alice", "alice@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        AppointmentDTO appointment = new AppointmentDTO(
                UUID.randomUUID().toString(), request.ownerId().toString(), request.inviteeName(), request.inviteeEmail(),
                request.appointmentDate(), request.duration()
        );
        BookAppointmentResponse response = new BookAppointmentResponse(appointment);
        when(bookAppointmentUseCase.execute(any(BookAppointmentRequest.class))).thenReturn(response);
        MvcResult result = mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        BookAppointmentResponse actual = mapper.readValue(result.getResponse().getContentAsString(), BookAppointmentResponse.class);
        assertEquals(response.appointment().inviteeEmail(), actual.appointment().inviteeEmail());
        verify(bookAppointmentUseCase, times(1)).execute(any(BookAppointmentRequest.class));
    }

    @Test
    void bookAppointment_invalidRequest_throws400() throws Exception {
        BookAppointmentRequest request = new BookAppointmentRequest(
                null, "", "invalid-email",
                LocalDate.now().minusDays(1),
                null
        );
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.ownerId").value("Owner ID cannot be null"))
                .andExpect(jsonPath("$.errors.inviteeName").value("Invitee name cannot be blank"))
                .andExpect(jsonPath("$.errors.inviteeEmail").value("Invitee email should be valid"))
                .andExpect(jsonPath("$.errors.appointmentDate").value("Appointment date must be today or in the future"))
                .andExpect(jsonPath("$.errors.duration").value("Duration must be 60 minutes"));
    }

    @Test
    void bookAppointment_slotNotAvailable_throws400() throws Exception {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(), "Bob", "bob@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        var exception = new SlotNotAvailableException("Slot not available");
        when(bookAppointmentUseCase.execute(any(BookAppointmentRequest.class))).thenThrow(exception);
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(bookAppointmentUseCase, times(1)).execute(any(BookAppointmentRequest.class));
    }

    @Test
    void bookAppointment_genericException_throws500() throws Exception {
        BookAppointmentRequest request = new BookAppointmentRequest(
                UUID.randomUUID(), "Carol", "carol@example.com",
                LocalDate.now().plusDays(1),
                new TimeIntervalDTO(LocalTime.of(10, 0), LocalTime.of(11, 0))
        );
        var exception = new RuntimeException("Something went wrong");
        when(bookAppointmentUseCase.execute(any(BookAppointmentRequest.class))).thenThrow(exception);
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        verify(bookAppointmentUseCase, times(1)).execute(any(BookAppointmentRequest.class));
    }

    @Test
    void listUpcomingAppointments_success() throws Exception {
        UUID ownerId = UUID.randomUUID();
        AppointmentDTO appointment = new AppointmentDTO(
                UUID.randomUUID().toString(), ownerId.toString(), "Dave", "dave@example.com",
                LocalDate.now().plusDays(2), new TimeIntervalDTO(LocalTime.of(9, 0), LocalTime.of(10, 0))
        );
        ListUpcomingAppointmentsResponse response = new ListUpcomingAppointmentsResponse(List.of(appointment));
        when(listUpcomingAppointmentUseCase.execute(any(UUID.class))).thenReturn(response);
        MvcResult result = mockMvc.perform(get("/api/appointments", ownerId)
                        .header("uid", ownerId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        ListUpcomingAppointmentsResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ListUpcomingAppointmentsResponse.class);
        assertEquals(1, actual.appointments().size());
        verify(listUpcomingAppointmentUseCase, times(1)).execute(ownerId);
    }

    @Test
    void listUpcomingAppointments_invalidOwnerId_throws400() throws Exception {
        String ownerId = "invalid-uuid";
        mockMvc.perform(get("/api/appointments")
                        .header("uid", ownerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.uid").value("Invalid value: " + ownerId + ". Expected type: UUID"));
    }
}
