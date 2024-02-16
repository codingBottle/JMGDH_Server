package com.codingbottle.calendar.domain.team.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record TeamScheduleListReqDto(
        List<Long> membersId,
        Long teamId,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull
        LocalDate endDate

) {
}
