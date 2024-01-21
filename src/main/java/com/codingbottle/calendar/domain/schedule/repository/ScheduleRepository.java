package com.codingbottle.calendar.domain.schedule.repository;

import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s" +
            " WHERE s.member.id = :memberId" +
            " AND :date BETWEEN s.startDate AND s.endDate")
    List<Schedule> findByDate(@Param("date") LocalDate date, @Param("memberId") long memberId);

    @Query("SELECT s FROM Schedule s" +
            " WHERE s.member.id = :memberId" +
            " AND" +
            " (FUNCTION('YEAR', s.startDate) = :year AND FUNCTION('MONTH', s.startDate) = :month)" +
            " OR " +
            " (FUNCTION('YEAR', s.endDate) = :year AND FUNCTION('MONTH', s.endDate) = :month)")
    List<Schedule> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("memberId") long memberId);

    @Query("SELECT s.id FROM Schedule s WHERE s.repeatCode = :repeatCode")
    List<Long> findByRepeatCode(@Param("repeatCode") String repeatCode);
}