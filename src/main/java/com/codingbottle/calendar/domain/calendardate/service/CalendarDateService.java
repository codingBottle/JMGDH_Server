package com.codingbottle.calendar.domain.calendardate.service;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendardate.repository.CalendarDateRepository;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarDateService {
    private final CalendarDateRepository calendarDateRepository;
    private final MemberService memberService;

    @Transactional
    public CalendarDate findOrCreateByDate(LocalDate date, long memberId) {
        return findByDate(date, memberId).orElseGet(() -> create(date, memberId));
    }

    private CalendarDate create(LocalDate date, long memberId) {
        Member member = memberService.getById(memberId);
        CalendarDate calendarDate = new CalendarDate(date, member);
        return calendarDateRepository.save(calendarDate);
    }

    private Optional<CalendarDate> findByDate(LocalDate date, long memberId) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return calendarDateRepository.findByDate(year, month, day, memberId);
    }
}