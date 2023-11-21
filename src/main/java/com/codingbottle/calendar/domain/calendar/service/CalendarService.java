package com.codingbottle.calendar.domain.calendar.service;

import com.codingbottle.calendar.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
}