package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendardate.service.CalendarDateService;
import com.codingbottle.calendar.domain.daterepeater.DateRepeater;
import com.codingbottle.calendar.domain.daterepeater.RepeatInterval;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {
    private final CalendarDateService calendarDateService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void create(ScheduleCreateReqDto reqDto, long memberId) {
        String title = reqDto.title();
        RepeatInterval repeatInterval = reqDto.repeatInterval();
        Integer repeatCount = reqDto.repeatCount();
        LocalDate targetDate = reqDto.targetDate();

        List<LocalDate> targetDates;
        // 반복하지 않아도 되는 상황이라면 리스트에 하나의 날짜만 저장
        if (repeatInterval == null || repeatCount <= 0) {
            targetDates = List.of(targetDate);
        } else {
            targetDates = DateRepeater.repeat(targetDate, repeatInterval, repeatCount);
        }

        // targetDates에 해당하는 CalendarDate 모두를 대상으로 하는 Schedule을 생성함.
        List<Schedule> schedules = targetDates.stream()
                .map(date -> {
                    CalendarDate calendarDate = calendarDateService.findOrCreateByDate(date, memberId);
                    return reqDto.isAllDay()
                            ? Schedule.allDay(title, calendarDate)
                            : Schedule.notAllDay(title, reqDto.startTime(), reqDto.endTime(), calendarDate);
                })
                .toList();

        scheduleRepository.saveAll(schedules);
    }
}