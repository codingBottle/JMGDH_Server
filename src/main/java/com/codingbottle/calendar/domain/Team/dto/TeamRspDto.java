package com.codingbottle.calendar.domain.Team.dto;

import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamRspDto {
    private Long id;

    private String name;

    private MemberResponseDto leader;

    private List<MemberResponseDto> teamMemberList;
}
