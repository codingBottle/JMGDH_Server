package com.codingbottle.calendar.domain.Team.controller;

import com.codingbottle.calendar.domain.Team.dto.*;
import com.codingbottle.calendar.domain.Team.service.TeamService;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // 팀 생성
    @PostMapping
    public ResponseEntity createTeam(@Valid @RequestBody TeamCreateReqDto teamCreateReqDto,
                                     @AuthenticationPrincipal Long memberId) {
        teamService.createTeam(teamCreateReqDto, memberId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 팀 가져오기
    @GetMapping("/{teamId}")
    public RspTemplate<TeamRspDto> getTeam(@PathVariable("teamId") Long teamId) {
        TeamRspDto teamRspDto = teamService.findTeamRspDtoByTeamId(teamId);
        return new RspTemplate<>(HttpStatus.OK, "teamId로 팀 조회", teamRspDto);
    }

    // 멤버가 속한 팀 목록 가져오기
    @GetMapping
    public RspTemplate<List<TeamRspDto>> getTeams(@AuthenticationPrincipal Long memberId) {
        List<TeamRspDto> teamRspDtos = teamService.findTeamRspDtosByMemberId(memberId);
        return new RspTemplate<>(HttpStatus.OK, "소속 팀들 조회", teamRspDtos);
    }

    // 팀 업데이트
    @PatchMapping
    public ResponseEntity updateTeam(@Valid @RequestBody TeamUpdateReqDto teamUpdateReqDto,
                                     @AuthenticationPrincipal Long memberId) {
        teamService.updateTeam(teamUpdateReqDto, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 팀 삭제하기
    @DeleteMapping("/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal Long memberId) {
        teamService.deleteTeam(teamId, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 팀원들 스케쥴 조회
    @GetMapping("/schedule")
    public  ResponseEntity getTeamSchedule(@Valid @RequestBody TeamScheduleListReqDto teamScheduleListReqDto,
                                           @AuthenticationPrincipal Long memberId) {
        List<List<TeamScheduleRspDto>> teamScheduleList = teamService.findTeamSchedule(teamScheduleListReqDto, memberId);
        return new ResponseEntity(teamScheduleList, HttpStatus.OK);
    }

    // 팀원 이메일으로 검색하기
    @GetMapping("/members")
    public RspTemplate<List<MemberResponseDto>> searchTeamMember(@RequestParam Long teamId, @RequestParam String email) {
        List<MemberResponseDto> memberResponseDtos = teamService.findTeamMembers(teamId, email);
        return new RspTemplate<>(HttpStatus.OK, "팀원 조회", memberResponseDtos);
    }

    // 그룹 나가기
    @DeleteMapping("exit/{teamId}")
    public ResponseEntity deleteTeamMember(@PathVariable("teamId") Long teamId,
                                           @AuthenticationPrincipal Long memberId) {
        teamService.exitTeam(teamId, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 팀 초대 코드 생성
    @GetMapping("invitations/{teamId}")
    public RspTemplate<TeamCodeRspDto> getTeamCode(@PathVariable("teamId") Long teamId,
                                           @AuthenticationPrincipal Long memberId) {
        TeamCodeRspDto teamCodeRspDto = teamService.generateTeamCode(teamId, memberId);
        return new RspTemplate<>(HttpStatus.OK, "팀 초대코드 생성", teamCodeRspDto);
    }

    // 초대코드로 팀 가입
    @PostMapping("invitations/{teamCode}")
    public ResponseEntity joinTeam(@PathVariable("teamCode") String receiveTeamCode,
                                   @AuthenticationPrincipal Long memberId) {
        teamService.registerTeamMember(memberId, receiveTeamCode);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
