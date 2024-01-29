package com.codingbottle.calendar.domain.schedule.dto;

import com.codingbottle.calendar.domain.daterepeater.RepeatInterval;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleCreateReqDto(
        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
        @NotNull
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
        @NotNull
        LocalDate endDate,
        @NotNull(message = "종일 여부를 입력해주세요.")
        Boolean isAllDay,
        @JsonFormat(pattern = "HH:mm", shape = Shape.STRING)
        LocalTime timeOfStartDate,
        @JsonFormat(pattern = "HH:mm", shape = Shape.STRING)
        LocalTime timeOfEndDate,

        String colorCode,
        // 반복주기 repeatInterval을 반복횟수 repeatCount만큼 반복한다.
        RepeatInterval repeatInterval,
        Integer repeatCount
) {
}