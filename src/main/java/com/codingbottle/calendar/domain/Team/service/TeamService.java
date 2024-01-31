package com.codingbottle.calendar.domain.Team.service;

import com.codingbottle.calendar.domain.Team.dto.*;
import com.codingbottle.calendar.domain.Team.entity.Team;
import com.codingbottle.calendar.domain.Team.entity.TeamCode;
import com.codingbottle.calendar.domain.Team.entity.TeamMemberList;
import com.codingbottle.calendar.domain.Team.mapper.TeamMapper;
import com.codingbottle.calendar.domain.Team.mapper.TeamScheduleMapper;
import com.codingbottle.calendar.domain.Team.repository.TeamMemberListRepository;
import com.codingbottle.calendar.domain.Team.repository.TeamRepository;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.service.MemberService;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.service.ScheduleService;
import com.codingbottle.calendar.global.exception.common.BusinessException;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.codingbottle.calendar.global.utils.InvitationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final InvitationCodeGenerator invitationCodeGenerator;
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final TeamRepository teamRepository;
    private final TeamMemberListRepository teamMemberListRepository;
    private final TeamMapper teamMapper;
    private final TeamScheduleMapper teamScheduleMapper;
    private final MemberMapper memberMapper;

    // 팀 생성 메소드
    @Transactional
    public void createTeam(TeamCreateReqDto teamCreateReqDto, Long memberId) {
        TeamCode teamCode = TeamCode.builder()
                            .code(invitationCodeGenerator.generateInvitationCode())
                            .expirationTime(LocalDateTime.now().plusMinutes(10))
                            .build();

        Member member = memberService.getById(memberId);

        Team team = Team.builder()
                    .name(teamCreateReqDto.name())
                    .teamCode(teamCode)
                    .leader(member)
                    .build();

        TeamMemberList teamMemberList = TeamMemberList.builder()
                                        .team(team)
                                        .member(member)
                                        .build();

        teamMemberListRepository.save(teamMemberList);
        teamRepository.save(team);
    }

    // 팀 id로 팀 조회
    public Team findTeamByTeamId(Long teamId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Team team = teamOptional.orElseThrow(() ->new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        return team;
    }

    // 팀 id로 팀 조회(반환 타입 Dto)
    public TeamRspDto findTeamRspDtoByTeamId(Long teamId) {
        Team team = findTeamByTeamId(teamId);
        return teamMapper.teamToTeamRspDto(team, memberMapper);
    }

    // 해당 팀원이 소속된 모든 팀 조회
    public List<TeamRspDto> findTeamRspDtos(Long memberId) {
        List<TeamMemberList> teamMemberLists = teamMemberListRepository.findTeamMemberListByMember_Id(memberId);

        List<Team> teamList = new ArrayList<>();

        if(teamMemberLists != null) {
            for(TeamMemberList teamMemberList : teamMemberLists) {
                teamList.add(teamMemberList.getTeam());
            }
        }

        return teamMapper.teamsToTeamRspDtos(teamList, memberMapper);
    }

    // 팀 update 메소드
    @Transactional
    public void updateTeam(TeamUpdateReqDto teamUpdateReqDto, Long memberId) {
        Team team = findTeamByTeamId(teamUpdateReqDto.id());

        checkLeader(memberId, team);

        team.updateForm(teamUpdateReqDto);
        teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(Long teamId, Long memberId) {
        Team team = findTeamByTeamId(teamId);

        checkLeader(memberId, team);

        teamRepository.delete(team);
    }

    // 팀 캘린더 조회
    public List<List<TeamScheduleRspDto>> findTeamSchedule(TeamScheduleListReqDto teamScheduleListReqDto, Long userId) {
        List<List<TeamScheduleRspDto>> teamScheduleList = new ArrayList<>();
        List<Long> memberIdList = teamScheduleListReqDto.membersId();

        checkMembers(memberIdList, userId);

        for(Long memberId : memberIdList) {
            List<Schedule> schedule = scheduleService.findByStartDateToEndDate(teamScheduleListReqDto.startDate(), teamScheduleListReqDto.endDate(), memberId);
            teamScheduleList.add(teamScheduleMapper.scheduleListToTeamScheduleRspDtos(schedule));
        }

        return  teamScheduleList;
    }

    private void checkMembers(List<Long> memberIdList, Long userId) {
        if(!memberIdList.contains(userId))
            throw new BusinessException(ErrorCode.NOT_EXISTS_IN_TEAM);
    }

    private void checkLeader(Long memberId, Team team) {
        if(team.getLeader().getId() != memberId)
            throw new BusinessException(ErrorCode.TEAM_UPDATE_DENIED);
    }
}
