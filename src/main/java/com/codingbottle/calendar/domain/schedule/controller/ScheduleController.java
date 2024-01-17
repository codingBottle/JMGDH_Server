package com.codingbottle.calendar.domain.schedule.controller;

import com.codingbottle.calendar.domain.calendardate.entity.CalendarDate;
import com.codingbottle.calendar.domain.calendardate.repository.CalendarDateRepository;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleListRspDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleUpdateRsqDto;
import com.codingbottle.calendar.domain.schedule.service.ScheduleService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final CalendarDateRepository calendarDateRepository;

    // 특정 날짜에 일정 등록
    @PostMapping("/schedules")
    public ResponseEntity<RspTemplate<Void>> handleCreate(
            @RequestBody @Validated ScheduleCreateReqDto reqDto,
            Authentication authentication
    ) {
        scheduleService.create(reqDto, Long.parseLong(authentication.getName()));

        String rspMessage = reqDto.targetDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + " 일정 생성. 반복주기: "
                + reqDto.repeatInterval().description != null ? reqDto.repeatInterval().description : "없음"
                + ", 반복횟수: " + reqDto.repeatCount() + "회";
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, rspMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    // 특정 날짜의 일정목록 조회
    @GetMapping("/schedules/year/{year}/month/{month}/day/{day}")
    public RspTemplate<ScheduleListRspDto> handleGetADayOfSchedules(
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day,
            Authentication authentication
    ) {
        Optional<CalendarDate> calendarDate = calendarDateRepository.findByDateFetchSchedules(year, month, day, Long.parseLong(authentication.getName()));

        ScheduleListRspDto rspDto = ScheduleListRspDto.from(calendarDate);
        return new RspTemplate<>(HttpStatus.OK,
                LocalDate.of(year, month, day) + " 일정목록",
                rspDto);
    }

    //  일정 수정
    @PatchMapping("/schedules/{id}")
    public ResponseEntity<RspTemplate<Void>> handleUpdate(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRsqDto reqDto,
            Authentication authentication
    ) {
        scheduleService.update(id, reqDto, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "일정이 수정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    //  일정 삭제
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<RspTemplate<Void>> handleDelete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        scheduleService.delete(id, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "일정이 삭제되었습니다");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }
}