package com.codingbottle.calendar.global.utils;

import com.codingbottle.calendar.global.exception.common.BusinessException;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.google.api.client.util.DateTime;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class GoogleCalendarConverter {
    public LocalDate convertDateTimeToLocalDate(DateTime dateTime) {
        if (dateTime == null) {
            throw new BusinessException(ErrorCode.DATETIME_IS_NULL);
        }
        // Convert to Instant
        Instant instant = Instant.ofEpochMilli(dateTime.getValue());

        // Convert to LocalDate
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }

    public LocalTime convertDateTimeToLocalTime(DateTime date) {
        if (date == null) {
            throw new BusinessException(ErrorCode.DATETIME_IS_NULL);
        }

        // Convert to Instant
        Instant instant = Instant.ofEpochMilli(date.getValue());

        // Convert to LocalDateTime
        LocalTime localTime = LocalTime.ofInstant(instant, ZoneId.systemDefault());


        return localTime;
    }
}
