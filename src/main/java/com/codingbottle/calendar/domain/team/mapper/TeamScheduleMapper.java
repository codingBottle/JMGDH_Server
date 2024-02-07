package com.codingbottle.calendar.domain.team.mapper;

import com.codingbottle.calendar.domain.team.dto.TeamScheduleRspDto;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamScheduleMapper {
    default TeamScheduleRspDto scheduleToTeamScheduleRspDto(Schedule schedule) {
        String startDate = schedule.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeOfStartDate = schedule.getTimeOfStartDate() == null ? LocalTime.of(0,0).format(DateTimeFormatter.ofPattern("HH:mm")) : schedule.getTimeOfStartDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endDate = schedule.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeOfEndDate = schedule.getTimeOfEndDate() == null ? LocalTime.of(23,30).format(DateTimeFormatter.ofPattern("HH:mm")) : schedule.getTimeOfEndDate().format(DateTimeFormatter.ofPattern("HH:mm"));


        TeamScheduleRspDto teamScheduleRspDto = new TeamScheduleRspDto(
                startDate,
                timeOfStartDate,
                endDate,
                timeOfEndDate,
                schedule.isAllDay()
        );

        return teamScheduleRspDto;
    }

    default List<TeamScheduleRspDto> scheduleListToTeamScheduleRspDtos(List<Schedule> scheduleList) {
        List<TeamScheduleRspDto> teamScheduleRspDtoList = new ArrayList<>();

        for(Schedule schedule : scheduleList) {
            teamScheduleRspDtoList.add(scheduleToTeamScheduleRspDto(schedule));
        }

        return teamScheduleRspDtoList;
    }
}
