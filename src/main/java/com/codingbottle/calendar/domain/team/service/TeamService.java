package com.codingbottle.calendar.domain.team.service;

import com.codingbottle.calendar.domain.team.dto.*;
import com.codingbottle.calendar.domain.team.entity.Team;
import com.codingbottle.calendar.domain.team.entity.TeamCode;
import com.codingbottle.calendar.domain.team.entity.TeamMember;
import com.codingbottle.calendar.domain.team.mapper.TeamCodeMapper;
import com.codingbottle.calendar.domain.team.mapper.TeamMapper;
import com.codingbottle.calendar.domain.team.mapper.TeamScheduleMapper;
import com.codingbottle.calendar.domain.team.repository.TeamCodeRepository;
import com.codingbottle.calendar.domain.team.repository.TeamMemberRepository;
import com.codingbottle.calendar.domain.team.repository.TeamRepository;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
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

    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamCodeRepository teamCodeRepository;
    private final TeamMapper teamMapper;
    private final TeamScheduleMapper teamScheduleMapper;
    private final MemberMapper memberMapper;
    private final TeamCodeMapper teamCodeMapper;

    // 팀 생성 메소드
    @Transactional
    public void createTeam(TeamCreateReqDto teamCreateReqDto, Long memberId) {
        Member member = memberService.getById(memberId);

        Team team = Team.builder()
                    .name(teamCreateReqDto.name())
                    .leader(member)
                    .build();

        TeamCode teamCode = TeamCode.builder()
                .code(InvitationCodeGenerator.generateInvitationCode())
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .team(team)
                .build();

        List<TeamMemberRspDto> teamRspDtos = findTeamRspDtosByMemberId(memberId);

        TeamMember teamMember = TeamMember.builder()
                                        .team(team)
                                        .member(member)
                                        .teamSequence(teamRspDtos.size()+1)
                                        .build();

        team.setTeamCode(teamCode);
        team.addTeamMemberLists(teamMember);

        teamRepository.save(team);
    }

    // 팀 id로 팀 조회
    private Team findTeamByTeamId(Long teamId) {
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        return teamOptional.orElseThrow(() ->new BusinessException(ErrorCode.TEAM_NOT_FOUND));
    }

    // 팀 id로 팀 조회(반환 타입 Dto)
    public TeamRspDto findTeamRspDtoByTeamId(Long teamId) {
        Team team = findTeamByTeamId(teamId);
        return teamMapper.teamToTeamRspDto(team, memberMapper);
    }

    // 해당 팀원이 소속된 모든 팀 조회
    public List<TeamMemberRspDto> findTeamRspDtosByMemberId(Long memberId) {
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMemberListByMember_Id(memberId);

        List<TeamMemberRspDto> teamMemberRspDtos = new ArrayList<>();

        if(teamMembers != null) {
            for(TeamMember teamMember : teamMembers) {
                teamMemberRspDtos.add(teamMapper.teamMemberToTeamMemberRspDto(teamMember, memberMapper));
            }
        }

        return teamMemberRspDtos;
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

    public List<MemberResponseDto> findTeamMembers(Long teamId, String email) {
        List<TeamMember> teamMember = teamMemberRepository.findTeamMemberByTeamIdAndEmail(teamId, email);
        List<Member> memberList = teamMember.stream().map(TeamMember::getMember).toList();

        return memberMapper.membersToMemberResponseDtos(memberList);
    }

    @Transactional
    public void exitTeam(Long teamId, Long memberId) {
        Team team = findTeamByTeamId(teamId);

        if(team.getLeader().getId() == memberId)
            throw new BusinessException(ErrorCode.LEADER_CANNOT_LEAVE_TEAM);

        // 리더는 팀 나가기 대신 팀 삭제하기 기능이 실행 되도록
        Optional<TeamMember> optionalTeamMemberList = teamMemberRepository.findTeamMemberListByMember_IdAndTeam_Id(memberId, teamId);
        TeamMember teamMember = optionalTeamMemberList.orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_IN_TEAM));

        teamMemberRepository.delete(teamMember);
    }

    @Transactional
    public TeamCodeRspDto generateTeamCode(Long teamId, Long memberId) {
        Team team = findTeamByTeamId(teamId);
        checkLeader(memberId, team);

        TeamCode teamCode = teamCodeRepository.findTeamCodeByTeam_Id(teamId);
        teamCode.generateTeamCode();

        return teamCodeMapper.teamCodeToTeamCodeRspDto(teamCode);
    }

    @Transactional
    public void registerTeamMember(Long memberId, String receiveTeamCode) {
        Optional<Team> optionalTeam = teamRepository.findTeamByTeamCode(receiveTeamCode);
        Team team = optionalTeam.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INVITE_CODE));

        if(Boolean.TRUE.equals(checkTeamCode(team, memberId))) {
            Member member = memberService.getById(memberId);

            List<TeamMemberRspDto> teamRspDtos = findTeamRspDtosByMemberId(memberId);

            TeamMember teamMember = TeamMember.builder()
                                            .team(team)
                                            .member(member)
                                            .teamSequence(teamRspDtos.size()+1)
                                            .build();

            team.addTeamMemberLists(teamMember);
        }
    }

    private Boolean checkTeamCode(Team team, Long memberId) {
        if(team.getTeamCode().getExpirationTime().isBefore(LocalDateTime.now())) // 코드 만료시간 확인
            throw new BusinessException(ErrorCode.TEAM_CODE_EXPIRED);
        else if(checkTeamMemberListInMember(team.getTeamMembers(), memberId)) // 이미 가입되어 있는지
            throw new BusinessException(ErrorCode.ALREADY_IN_TEAM);
        return true;
    }

    private Boolean checkMembers(List<Long> memberIdList, Long userId) {
        if(!memberIdList.contains(userId))
            throw new BusinessException(ErrorCode.NOT_EXISTS_IN_TEAM);

        return true;
    }
    private Boolean checkLeader(Long memberId, Team team) {
        if(team.getLeader().getId() != memberId)
            throw new BusinessException(ErrorCode.TEAM_UPDATE_DENIED);

        return true;
    }
    private Boolean checkTeamMemberListInMember(List<TeamMember> memberLists, Long memberId) {
        for(TeamMember teamMember : memberLists) {
            if(teamMember.getMember().getId() == memberId)
                return true;
        }
        return false;
    }
}
