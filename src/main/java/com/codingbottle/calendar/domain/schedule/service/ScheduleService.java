package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.calendar.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendar.service.CalendarDateService;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {
    private final CalendarDateService calendarDateService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void create(ScheduleCreateReqDto reqDto, long memberId) {
        // Date로 찾거나 생성
        CalendarDate calendarDate = calendarDateService.findOrCreateByDate(reqDto.date(), memberId);

        Schedule schedule;
        if (reqDto.isAllDay()) {
            schedule = Schedule.allDay(reqDto.title(), calendarDate);
        } else {
            schedule = Schedule.notAllDay(reqDto.title(), reqDto.startTime(), reqDto.endTime(), calendarDate);
        }

        scheduleRepository.save(schedule);
    }
}