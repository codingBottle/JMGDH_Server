package com.codingbottle.calendar.domain.calendardate.controller;

import com.codingbottle.calendar.domain.calendardate.dto.MonthlyCalendarReqDto;
import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendardate.repository.CalendarDateRepository;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CalendarDateController {

    private final CalendarDateRepository calendarDateRepository;

    @GetMapping("/calendars/year/{year}/month/{month}")
    public RspTemplate<MonthlyCalendarReqDto> handleGetMonthlyCalendar(
            @PathVariable int year,
            @PathVariable int month,
             Authentication authentication
    ) {
        List<CalendarDate> calendarDates = calendarDateRepository.findByMemberIdAndYearAndMonth(year, month,
                Long.parseLong(authentication.getName()));
        MonthlyCalendarReqDto rspDto = MonthlyCalendarReqDto.from(calendarDates);

        return new RspTemplate<>(HttpStatus.OK,
                year +"년 " + month + "월 월별 캘린더",
                rspDto);
    }
}
