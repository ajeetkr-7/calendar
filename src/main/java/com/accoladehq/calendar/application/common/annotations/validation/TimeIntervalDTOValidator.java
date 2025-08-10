package com.accoladehq.calendar.application.common.annotations.validation;

import java.time.temporal.ChronoUnit;

import com.accoladehq.calendar.application.common.dto.TimeIntervalDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeIntervalDTOValidator implements ConstraintValidator<ValidTimeInterval, TimeIntervalDTO> {

    private int diff;
    private int min;
    private int max;

    @Override
    public void initialize(ValidTimeInterval constraintAnnotation) {
        this.diff = constraintAnnotation.diff();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(TimeIntervalDTO duration, ConstraintValidatorContext context) {
        if (duration == null) {
            return false;
        }

        // Checking if start and end times represent whole hours
        if (duration.startTime().getMinute() != 0 || duration.startTime().getSecond() != 0) {
            return false;
        }
        if (duration.endTime().getMinute() != 0 || duration.endTime().getSecond() != 0) {
            return false;
        }

        long minutesBetween = ChronoUnit.MINUTES.between(duration.startTime(), duration.endTime());

        if (min > -1 && minutesBetween < min) {
            return false;
        }
        if (max > -1 && minutesBetween > max) {
            return false;
        }
        return diff <= -1 || minutesBetween == diff;
    }
}