package com.codingbottle.calendar.domain.schedule.dto;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class ScheduleListRspDto {
    List<ScheduleDto> schedules;

    // Optional이 비어있으면, 빈 SchueduleDto 리스트 반환
    public static ScheduleListRspDto from(Optional<CalendarDate> calendarDate) {
        List<ScheduleDto> scheduleDtos = calendarDate
                .map(CalendarDate::getSchedules)
                .map(ScheduleDto::from)
                .orElseGet(List::of);
        return new ScheduleListRspDto(scheduleDtos);
    }

    @Getter
    static class ScheduleDto {
        long id;
        String title;
        String startTime;
        String endTime;
        boolean isAllDay;
        ScheduleDto(Schedule schedule) {
            this.id = schedule.getId();
            this.title = schedule.getTitle();
            this.startTime = schedule.getStartTime() == null ? "" : schedule.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.endTime = schedule.getEndTime() == null ? "" : schedule.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.isAllDay = schedule.isAllDay();
        }

        static List<ScheduleDto> from(List<Schedule> schedules) {
            return schedules.stream()
                    .map(ScheduleDto::new)
                    .toList();
        }
    }
}
