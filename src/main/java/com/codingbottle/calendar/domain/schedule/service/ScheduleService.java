package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendardate.repository.CalendarDateRepository;
import com.codingbottle.calendar.domain.calendardate.service.CalendarDateService;
import com.codingbottle.calendar.domain.daterepeater.DateRepeater;
import com.codingbottle.calendar.domain.daterepeater.RepeatInterval;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleUpdateRsqDto;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {
    private final CalendarDateService calendarDateService;
    private final ScheduleRepository scheduleRepository;
    private final CalendarDateRepository calendarDateRepository;

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

    //  일정 수정
    @Transactional
    public void update(Long scheduleId, ScheduleUpdateRsqDto reqDto, long memberId) {
        // 해당 id의 일정이 없으면 예외 처리
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다."));

        // 회원이 일정을 수정할 권한이 있는지 확인
        if (schedule.getCalendarDate().getMember().getMemberId() != memberId) {
            throw new IllegalStateException("일정을 수정할 권한이 없습니다.");
        }

        // 기존 CalendarDate를 가져옴
        CalendarDate calendarDate = schedule.getCalendarDate();

        // 기존 CalendarDate의 날짜 정보 업데이트
        calendarDate.updateDate(reqDto.targetDate());

        // Schedule 엔티티 업데이트
        schedule.updateFrom(reqDto.toScheduleEntity(calendarDate));

        // 변경된 Schedule과 CalendarDate 저장
        scheduleRepository.save(schedule);
        calendarDateRepository.save(calendarDate);
    }

    //  일정 삭제
    @Transactional
    public void delete(long scheduleId, long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다. ID: " + scheduleId));

        if (schedule.getCalendarDate().getMember().getMemberId() != memberId) {
            throw new IllegalStateException("일정을 삭제할 권한이 없습니다.");
        }

        // 기존 CalendarDate 가져오기
        CalendarDate calendarDate = schedule.getCalendarDate();

        // Schedule 삭제 & 연결된 CalendarDate 삭제
        scheduleRepository.delete(schedule);
        calendarDateRepository.delete(calendarDate);
    }
}