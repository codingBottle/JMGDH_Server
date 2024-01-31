package com.codingbottle.calendar.domain.Team.controller;

import com.codingbottle.calendar.domain.Team.dto.*;
import com.codingbottle.calendar.domain.Team.service.TeamService;
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

    @PostMapping
    public ResponseEntity createTeam(@Valid @RequestBody TeamCreateReqDto teamCreateReqDto,
                                     @AuthenticationPrincipal Long memberId) {
        teamService.createTeam(teamCreateReqDto, memberId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{teamId}")
    public RspTemplate<TeamRspDto> getTeam(@PathVariable("teamId") Long teamId) {
        TeamRspDto teamRspDto = teamService.findTeamRspDtoByTeamId(teamId);
        return new RspTemplate<>(HttpStatus.OK, "teamId로 팀 조회", teamRspDto);
    }

    @GetMapping
    public RspTemplate<List<TeamRspDto>> getTeams(@AuthenticationPrincipal Long memberId) {
        List<TeamRspDto> teamRspDtos = teamService.findTeamRspDtos(memberId);
        return new RspTemplate<>(HttpStatus.OK, "팀 조회", teamRspDtos);
    }

    @PatchMapping
    public ResponseEntity updateTeam(@Valid @RequestBody TeamUpdateReqDto teamUpdateReqDto,
                                     @AuthenticationPrincipal Long memberId) {
        teamService.updateTeam(teamUpdateReqDto, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal Long memberId) {
        teamService.deleteTeam(teamId, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/schedule")
    public  ResponseEntity getTeamSchedule(@Valid @RequestBody TeamScheduleListReqDto teamScheduleListReqDto,
                                           @AuthenticationPrincipal Long memberId) {
        List<List<TeamScheduleRspDto>> teamScheduleList = teamService.findTeamSchedule(teamScheduleListReqDto, memberId);
        return new ResponseEntity(teamScheduleList, HttpStatus.OK);
    }

    // 팀원 이름으로 검색하기
    // 그룹 나가기
    // 초대코드 발송
    // 초대코드로 팀원 수락
}
