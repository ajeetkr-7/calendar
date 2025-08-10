package com.accoladehq.calendar.infrastructure.persistence.projections;

import java.time.LocalTime;

public interface TimeIntervalProjection {

    LocalTime getStartTime();

    LocalTime getEndTime();
}
