package com.codingbottle.calendar.domain.schedule.entity;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private LocalTime startTime;
    @Column(nullable = true)
    private LocalTime endTime;
    @Column(nullable = false)
    private boolean isAllDay;

    @ManyToOne
    @JoinColumn(name = "calendar_date_id", nullable = false)
    private CalendarDate calendarDate;

    public static Schedule notAllDay(String title, LocalTime startTime, LocalTime endTime, CalendarDate calendarDate){
        return new Schedule(title, startTime, endTime, calendarDate);
    }

    public static Schedule allDay(String title, CalendarDate calendarDate){
        return new Schedule(title, calendarDate);
    }

    private Schedule(String title, LocalTime startTime, LocalTime endTime, CalendarDate calendarDate) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarDate = calendarDate;
        this.isAllDay = false;
    }

    private Schedule(String title, CalendarDate calendarDate) {
        this.title = title;
        this.calendarDate = calendarDate;
        this.isAllDay = true;
    }

    //  update
    public void updateFrom(Schedule updatedSchedule) {
        this.title = updatedSchedule.title;
        this.startTime = updatedSchedule.startTime;
        this.endTime = updatedSchedule.endTime;
        this.isAllDay = updatedSchedule.isAllDay;
        this.calendarDate = updatedSchedule.calendarDate;
    }
}