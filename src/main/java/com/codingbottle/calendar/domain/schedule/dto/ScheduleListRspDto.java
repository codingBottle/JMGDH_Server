package com.codingbottle.calendar.domain.schedule.dto;

import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Getter
public class ScheduleListRspDto {
    List<ScheduleDto> schedules;

    // Optional이 비어있으면, 빈 SchueduleDto 리스트 반환
    public static ScheduleListRspDto from(List<Schedule> schedules) {
        return new ScheduleListRspDto(ScheduleDto.from(schedules));
    }

    @Getter
    static class ScheduleDto {
        long id;
        String title;
        String colorCode;

        String startDate;
        String timeOfStartDate;
        String endDate;
        String timeOfEndDateTime;

        boolean isRepeat;
        boolean isAllDay;
        ScheduleDto(Schedule schedule) {
            this.id = schedule.getId();
            this.title = schedule.getTitle();
            this.colorCode = schedule.getColorCode();

            this.timeOfStartDate = schedule.getTimeOfStartDate() == null ? "" : schedule.getTimeOfStartDate().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.timeOfEndDateTime = schedule.getTimeOfEndDate() == null ? "" : schedule.getTimeOfEndDate().format(DateTimeFormatter.ofPattern("HH:mm"));

            this.startDate = schedule.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.endDate = schedule.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            this.isRepeat = schedule.isRepeat();
            this.isAllDay = schedule.isAllDay();
        }

        static List<ScheduleDto> from(List<Schedule> schedules) {
            return schedules.stream()
                    .map(ScheduleDto::new)
                    .toList();
        }
    }
}
