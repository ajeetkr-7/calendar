package com.accoladehq.calendar.presentation.dto;

import lombok.RequiredArgsConstructor;

public record ErrorResponse(
        String message
) {
    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }
}
