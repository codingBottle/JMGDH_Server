package com.codingbottle.calendar.domain.schedule.controller;

import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleListRspDto;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleUpdateReqDto;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.service.ScheduleService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;

    // 특정 날짜에 일정 등록
    @PostMapping("/schedules")
    public ResponseEntity<RspTemplate<Void>> handleCreate(
            @RequestBody @Validated ScheduleCreateReqDto reqDto,
            Authentication authentication
    ) {
        scheduleService.create(reqDto, Long.parseLong(authentication.getName()));

        String rspMessage = reqDto.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + " 일정 생성. 반복주기: "
                + (reqDto.repeatInterval() != null ? reqDto.repeatInterval().description : "없음")
                + ", 반복횟수: " + (reqDto.repeatCount() != null ? String.valueOf(reqDto.repeatCount()) : 0 + "회");

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, rspMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    //  일정 수정
    @PatchMapping("/schedules/{id}")
    public ResponseEntity<RspTemplate<Void>> handleUpdate(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateReqDto reqDto,
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
            @RequestParam(defaultValue = "false") boolean repeat,
            Authentication authentication
    ) {
        scheduleService.delete(id, repeat, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "일정이 삭제되었습니다");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 연-월-일 조회
    @GetMapping("/schedules/year/{year}/month/{month}/day/{day}")
    public RspTemplate<ScheduleListRspDto> handleGetADayOfSchedules(
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day,
            Authentication authentication
    ) {
        LocalDate dateForSearch = LocalDate.of(year, month, day);
        List<Schedule> schedules = scheduleService.findByDate(dateForSearch, Long.parseLong(authentication.getName()));

        ScheduleListRspDto rspDto = ScheduleListRspDto.from(schedules);
        return new RspTemplate<>(HttpStatus.OK,
                LocalDate.of(year, month, day) + " 일정목록",
                rspDto);
    }

    // 두 날짜 사이의 일정 조회 (주간일정 조회에 사용)
    @GetMapping("/schedules/start-date/{startDate}/end-date/{endDate}")
    public RspTemplate<ScheduleListRspDto> handleGetSchedulesBetWeenTwoDays(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Authentication authentication
    ) {
        List<Schedule> schedules = scheduleService.findByStartDateToEndDate(startDate, endDate, Long.parseLong(authentication.getName()));

        ScheduleListRspDto rspDto = ScheduleListRspDto.from(schedules);
        return new RspTemplate<>(HttpStatus.OK,
                startDate + "부터 " + endDate + "사이의 일정 목록",
                rspDto);
    }

    // 연-월로 조회
    @GetMapping("/schedules/year/{year}/month/{month}")
    public RspTemplate<ScheduleListRspDto> handleGetMonthlyCalendar(
            @PathVariable int year,
            @PathVariable int month,
            Authentication authentication
    ) {
        List<Schedule> schedules = scheduleService.findByYearAndMonth(year, month,
                Long.parseLong(authentication.getName()));
        ScheduleListRspDto rspDto = ScheduleListRspDto.from(schedules);

        return new RspTemplate<>(HttpStatus.OK,
                year +"년 " + month + "월 월별 캘린더",
                rspDto);
    }
}