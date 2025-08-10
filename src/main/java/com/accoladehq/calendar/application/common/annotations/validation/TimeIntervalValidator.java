package com.accoladehq.calendar.application.common.annotations.validation;

import java.time.temporal.ChronoUnit;

import com.accoladehq.calendar.domain.model.TimeInterval;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeIntervalValidator implements ConstraintValidator<ValidTimeInterval, TimeInterval> {

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
    public boolean isValid(TimeInterval duration, ConstraintValidatorContext context) {
        if (duration == null || duration.getStartTime() == null || duration.getEndTime() == null) {
            return false;
        }

        // Checking if start and end times represent whole hours
        if (duration.getStartTime().getMinute() != 0 || duration.getStartTime().getSecond() != 0) {
            return false;
        }
        if (duration.getEndTime().getMinute() != 0 || duration.getEndTime().getSecond() != 0) {
            return false;
        }

        long minutesBetween = ChronoUnit.MINUTES.between(duration.getStartTime(), duration.getEndTime());

        if (min > -1 && minutesBetween < min) {
            return false;
        }
        if (max > -1 && minutesBetween > max) {
            return false;
        }
        return diff <= -1 || minutesBetween == diff;
    }
}
