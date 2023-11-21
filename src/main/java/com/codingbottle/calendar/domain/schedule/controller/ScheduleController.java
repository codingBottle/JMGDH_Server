package com.codingbottle.calendar.domain.schedule.controller;

import com.codingbottle.calendar.global.api.RspTemplate;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<RspTemplate<Void>> create(
            @RequestBody ScheduleCreateReqDto reqDto,
            Authentication authentication
    ) {
        scheduleService.create(reqDto, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                reqDto.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        + " 일정 생성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }
}