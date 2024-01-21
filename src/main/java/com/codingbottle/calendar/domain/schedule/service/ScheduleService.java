package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.daterepeater.RepeatInterval;
import com.codingbottle.calendar.domain.daterepeater.ScheduleRepeater;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleUpdateReqDto;
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
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(ScheduleCreateReqDto reqDto, long memberId) {
        String title = reqDto.title();
        RepeatInterval repeatInterval = reqDto.repeatInterval();
        Integer repeatCount = reqDto.repeatCount();
        LocalDate startDate = reqDto.startDate();
        LocalDate endDate = reqDto.endDate();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        // 종일 여부 검사
        Schedule schedule;
        if (reqDto.isAllDay()) {
            schedule = Schedule.allDay(member, title, startDate, endDate);
        } else {
            schedule = Schedule.notAllDay(member, title, startDate, endDate, reqDto.timeOfStartDate(), reqDto.timeOfEndDate());
        }

        // 반복주기에 따라 단일 일정 or 반복 일정 생성
        List<Schedule> schedules;
        if (repeatInterval == null || repeatCount <= 0) {
            schedules = List.of(schedule);
        } else {
            schedules = ScheduleRepeater.repeat(schedule, repeatInterval, repeatCount);
        }

        scheduleRepository.saveAll(schedules);
    }

    //  일정 수정
    @Transactional
    public void update(Long scheduleId, ScheduleUpdateReqDto reqDto, long memberId) {
        // 해당 id의 일정이 없으면 예외 처리
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다."));

        // 회원이 일정을 수정할 권한이 있는지 확인
        Member member = schedule.getMember();
        if (member.getId() != memberId) {
            throw new IllegalStateException("일정을 수정할 권한이 없습니다.");
        }

        // Schedule 엔티티 업데이트
        schedule.updateFrom(reqDto.toScheduleEntity(member));
    }

    //  일정 삭제
    @Transactional
    public void delete(long scheduleId, boolean repeat, long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다. ID: " + scheduleId));

        if (schedule.getMember().getId() != memberId) {
            throw new IllegalStateException("일정을 삭제할 권한이 없습니다.");
        }

        // 반복 삭제일 경우는 연관 일정을 모두 삭제하기 위해
        // 반복코드를 기준으로 삭제
        if (repeat) {
           List<Long> schedules = scheduleRepository.findByRepeatCode(schedule.getRepeatCode());
           scheduleRepository.deleteAllByIdInBatch(schedules); // Persistence Context를 사용하지 않으므로 Cascade Delete 등이 동작하지 않음에 주의
        } else {
            // 단일 일정이면, 해당 일정만 삭제
            scheduleRepository.delete(schedule);
        }
    }

    public List<Schedule> findByDate(LocalDate dateForSearch, long memberId) {
        return scheduleRepository.findByDate(dateForSearch, memberId);
    }

    public List<Schedule> findByYearAndMonth(int year, int month, long memberId) {
        return scheduleRepository.findByYearAndMonth(year, month, memberId);
    }
}