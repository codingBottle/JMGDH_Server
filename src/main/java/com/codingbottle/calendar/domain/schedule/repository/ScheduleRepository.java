package com.codingbottle.calendar.domain.schedule.repository;

import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}