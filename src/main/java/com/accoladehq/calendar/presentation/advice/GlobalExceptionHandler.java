package com.accoladehq.calendar.presentation.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HandledErrorResponse handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return new HandledErrorResponse(errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public HandledErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ex.getName(), "Invalid value: " + ex.getValue() + ". Expected type: " + ex.getRequiredType().getSimpleName());
        return new HandledErrorResponse(errorMap);
    }

    public record HandledErrorResponse(
            Map<String, String> errors
            ) {

    }

}
