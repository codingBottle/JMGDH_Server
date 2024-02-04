package com.codingbottle.calendar.domain.Team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TeamCodeRspDto {
    private Long id;

    private String code;

    private LocalDateTime expirationTime;

    private Long teamId;
}
