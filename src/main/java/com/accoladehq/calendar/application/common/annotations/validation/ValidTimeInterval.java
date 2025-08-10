package com.accoladehq.calendar.application.common.annotations.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = {TimeIntervalValidator.class, TimeIntervalDTOValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidTimeInterval {

    int diff() default -1;

    int min() default -1;

    int max() default -1;

    String message() default "Invalid time interval";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
