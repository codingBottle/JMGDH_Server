package com.codingbottle.calendar.domain.Team.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record TeamScheduleListReqDto(
        List<Long> membersId,
        Long teamId,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate

) {
}
