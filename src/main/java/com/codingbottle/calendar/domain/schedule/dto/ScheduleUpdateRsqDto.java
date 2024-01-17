package com.codingbottle.calendar.domain.schedule.dto;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleUpdateRsqDto(
        @NotBlank(message = "제목을 입력해주세요.") String title,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull LocalDate targetDate,
        @NotNull(message = "종일 여부를 입력해주세요.") Boolean isAllDay,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING) LocalTime startTime,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING) LocalTime endTime
) {
    public Schedule toScheduleEntity(CalendarDate calendarDate) {
        if (isAllDay) {
            return Schedule.allDay(title, calendarDate);
        } else {
            return Schedule.notAllDay(title, startTime, endTime, calendarDate);
        }
    }
}
