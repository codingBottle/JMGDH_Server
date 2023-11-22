package com.codingbottle.calendar.domain.calendardate.dto;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MonthlyCalendarResponse {
    @JsonProperty("calendarDates")
    List<CalendarDateDto> calendarDateDtos;

    public static MonthlyCalendarResponse from(List<CalendarDate> calendarDates) {
        return new MonthlyCalendarResponse(calendarDates);
    }

    public MonthlyCalendarResponse(List<CalendarDate> calendarDates) {
        this.calendarDateDtos = calendarDates.stream()
                .map(CalendarDateDto::new)
                .toList();
    }

    @Getter
    static class CalendarDateDto {
        long id;
        int day;
        List<ScheduleDto> schedules;
        CalendarDateDto(CalendarDate calendarDate) {
            this.id = calendarDate.getId();
            this.day = calendarDate.getDay();
            this.schedules = calendarDate.getSchedules()
                    .stream().map(ScheduleDto::new)
                    .toList();
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
        }
    }


}
