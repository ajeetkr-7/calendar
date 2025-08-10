package com.accoladehq.calendar.infrastructure.mapper;

import com.accoladehq.calendar.domain.model.TimeInterval;
import com.accoladehq.calendar.infrastructure.persistence.projections.TimeIntervalProjection;

public class TimeIntervalMapper {

    public static TimeInterval from(TimeIntervalProjection projection) {
        if (projection == null) {
            return null;
        }
        return new TimeInterval(
                projection.getStartTime(),
                projection.getEndTime()
        );
    }
}
