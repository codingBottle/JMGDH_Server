package com.codingbottle.calendar.domain.Team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamScheduleRspDto {
    String startDate;
    String timeOfStartDate;
    String endDate;
    String timeOfEndDateTime;

    boolean isAllDay;


}
