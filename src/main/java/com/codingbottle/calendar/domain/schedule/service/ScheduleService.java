package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
}