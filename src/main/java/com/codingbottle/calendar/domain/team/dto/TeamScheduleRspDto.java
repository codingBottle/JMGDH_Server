package com.codingbottle.calendar.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamScheduleRspDto {
    String startDate;
    String timeOfStartDate;
    String endDate;
    String timeOfEndDateTime;

    boolean isAllDay;


}
