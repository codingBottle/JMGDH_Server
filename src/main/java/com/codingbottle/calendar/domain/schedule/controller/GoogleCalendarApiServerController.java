package com.codingbottle.calendar.domain.schedule.controller;

import com.codingbottle.calendar.domain.schedule.service.CalendarApiIntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("google")
public class GoogleCalendarApiServerController {
    private final CalendarApiIntegrationService calendarApiIntegrationService;

    public GoogleCalendarApiServerController(CalendarApiIntegrationService calendarApiIntegrationService) {
        this.calendarApiIntegrationService = calendarApiIntegrationService;
    }

    @GetMapping("/authorize")
    public String authorizeGoogle() {
        return calendarApiIntegrationService.getRequestUrl();
    }

    @GetMapping("/callback")
    public ResponseEntity callback(@RequestParam("code") String code) throws IOException {
        calendarApiIntegrationService.scheduleIntegration(code);
        return new ResponseEntity(HttpStatus.OK);
    }
}
