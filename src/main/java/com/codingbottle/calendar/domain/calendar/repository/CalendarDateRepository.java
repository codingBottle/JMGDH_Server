package com.codingbottle.calendar.domain.calendar.repository;

import com.codingbottle.calendar.domain.calendar.entity.CalendarDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CalendarDateRepository extends JpaRepository<CalendarDate, Long> {
    @Query("SELECT cd FROM CalendarDate cd " +
            "WHERE cd.member.memberId = :memberId" +
            " and cd.year = :year" +
            " and cd.month = :month" +
            " and cd.day = :day")
    Optional<CalendarDate> findByDateAndMemberId(@Param("year") int year, @Param("month") int month,
                                                 @Param("day") int day, @Param("memberId") long memberId);
}