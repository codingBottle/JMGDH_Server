package com.codingbottle.calendar.domain.team.mapper;

import com.codingbottle.calendar.domain.team.dto.TeamMemberRspDto;
import com.codingbottle.calendar.domain.team.dto.TeamRspDto;
import com.codingbottle.calendar.domain.team.entity.Team;
import com.codingbottle.calendar.domain.team.entity.TeamMember;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    default TeamRspDto teamToTeamRspDto(Team team, MemberMapper memberMapper) {
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();

        for (TeamMember teamMember : team.getTeamMembers()) {
            memberResponseDtos.add(memberMapper.memberToMemberResponseDto(teamMember.getMember()));
        }

        return new TeamRspDto(
                team.getId(),
                team.getName(),
                memberMapper.memberToMemberResponseDto(team.getLeader()),
                memberResponseDtos
        );
    }

    default TeamMemberRspDto teamMemberToTeamMemberRspDto(TeamMember teamMember, MemberMapper memberMapper) {
        return new TeamMemberRspDto(
                teamMember.getId(),
                teamToTeamRspDto(teamMember.getTeam(), memberMapper),
                teamMember.getTeamSequence()
        );
    }
}
