package com.codingbottle.calendar.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberRspDto {
    private Long id;

    private TeamRspDto teamRspDto;

    private Integer teamSequence;
}
