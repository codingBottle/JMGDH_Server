package com.codingbottle.calendar.domain.Team.mapper;

import com.codingbottle.calendar.domain.Team.dto.TeamRspDto;
import com.codingbottle.calendar.domain.Team.entity.Team;
import com.codingbottle.calendar.domain.Team.entity.TeamMemberList;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    default TeamRspDto teamToTeamRspDto(Team team, MemberMapper memberMapper) {
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();

        for(TeamMemberList teamMemberList : team.getTeamMemberLists()) {
            memberResponseDtos.add(memberMapper.memberToMemberResponseDto(teamMemberList.getMember()));
        }

        TeamRspDto teamRspDto = new TeamRspDto(
                team.getId(),
                team.getName(),
                memberMapper.memberToMemberResponseDto(team.getLeader()),
                memberResponseDtos
        );

        return teamRspDto;
    }

    default List<TeamRspDto> teamsToTeamRspDtos(List<Team> teamList, MemberMapper memberMapper) {
        List<TeamRspDto> teamRspDtos = new ArrayList<>();

        for(Team team : teamList) {
            teamRspDtos.add(teamToTeamRspDto(team, memberMapper));
        }

        return teamRspDtos;
    }
}
