package com.codingbottle.calendar.domain.calendar.repository;

import com.codingbottle.calendar.domain.calendar.entity.CalendarDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarDate, Long> {
}