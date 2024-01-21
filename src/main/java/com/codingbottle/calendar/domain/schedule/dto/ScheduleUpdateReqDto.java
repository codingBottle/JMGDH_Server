package com.codingbottle.calendar.domain.schedule.dto;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleUpdateReqDto(
        @NotBlank(message = "제목을 입력해주세요.") String title,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull LocalDate endDate,
        @NotNull(message = "종일 여부를 입력해주세요.") Boolean isAllDay,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING) LocalTime timeOfStartDate,
        @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING) LocalTime timeOfEndDate
) {
    public Schedule toScheduleEntity(Member member) {
        if (isAllDay == null || !isAllDay) {
            return Schedule.notAllDay(member, title, startDate, endDate, timeOfStartDate, timeOfEndDate);
        } else {
            return Schedule.allDay(member, title, startDate, endDate);
        }
    }
}
