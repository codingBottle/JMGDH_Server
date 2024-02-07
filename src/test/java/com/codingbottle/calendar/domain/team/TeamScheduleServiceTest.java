package com.codingbottle.calendar.domain.team;

import com.codingbottle.calendar.domain.team.dto.TeamScheduleListReqDto;
import com.codingbottle.calendar.domain.team.dto.TeamScheduleRspDto;
import com.codingbottle.calendar.domain.team.mapper.TeamScheduleMapper;
import com.codingbottle.calendar.domain.team.service.TeamService;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import com.codingbottle.calendar.domain.schedule.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeamScheduleServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TeamScheduleMapper teamScheduleMapper;

    @Test
    public void testFindTeamSchedule() {
        // 테스트에 필요한 데이터 준비
        List<Long> memberIdList = new ArrayList<>();
        memberIdList.add(1L);
        memberIdList.add(2L);

        LocalDate startDate = LocalDate.of(2024, 1, 21);
        LocalDate endDate = LocalDate.of(2024, 1, 27);

        TeamScheduleListReqDto teamScheduleListReqDto = new TeamScheduleListReqDto(memberIdList, 1L, startDate, endDate);
        // teamScheduleListReqDto 설정

        Long userId = 1L;

        when(teamScheduleListReqDto.membersId()).thenReturn(memberIdList);

        // Mock ScheduleService의 메소드 호출에 대한 응답 설정
        List<Schedule> scheduleList1 = new ArrayList<>();
        // scheduleList1 설정
        Schedule schedule1 = Schedule.notAllDay(
                Member.builder().id(1L).email("rara").nickname("kill").build(),
                "테스트1",
                LocalDate.of(2024, 1, 22),
                LocalDate.of(2024, 1, 22),
                LocalTime.of(8, 30),
                LocalTime.of(10, 30),
                "FFFFF"
        );

        Schedule schedule2 = Schedule.notAllDay(
                Member.builder().id(1L).email("rara").nickname("kill").build(),
                "테스트1",
                LocalDate.of(2024, 1, 23),
                LocalDate.of(2024, 1, 23),
                LocalTime.of(8, 30),
                LocalTime.of(10, 30),
                "FFFFF"
        );

        scheduleList1.add(schedule1);
        scheduleList1.add(schedule2);

        List<Schedule> scheduleList2 = new ArrayList<>();
        // scheduleList2 설정

        Schedule schedule3 = Schedule.notAllDay(
                Member.builder().id(1L).email("rara").nickname("kill").build(),
                "테스트1",
                LocalDate.of(2024, 1, 27),
                LocalDate.of(2024, 1, 27),
                LocalTime.of(8, 30),
                LocalTime.of(22, 30),
                "FFFFF"
        );

        Schedule schedule4 = Schedule.notAllDay(
                Member.builder().id(1L).email("rara").nickname("kill").build(),
                "테스트1",
                LocalDate.of(2024, 1, 21),
                LocalDate.of(2024, 1, 21),
                LocalTime.of(8, 30),
                LocalTime.of(17, 30),
                "FFFFF"
        );

        scheduleList2.add(schedule3);
        scheduleList2.add(schedule4);

        when(scheduleService.findByStartDateToEndDate(teamScheduleListReqDto.startDate(), teamScheduleListReqDto.endDate(), memberIdList.get(0))).thenReturn(scheduleList1);
        when(scheduleService.findByStartDateToEndDate(teamScheduleListReqDto.startDate(), teamScheduleListReqDto.endDate(), memberIdList.get(1))).thenReturn(scheduleList2);

        // Mock TeamScheduleMapper의 메소드 호출에 대한 응답 설정
        List<TeamScheduleRspDto> teamScheduleRspDtoList1 = new ArrayList<>();
        // teamScheduleRspDtoList1 설정
        List<TeamScheduleRspDto> teamScheduleRspDtoList2 = new ArrayList<>();
        // teamScheduleRspDtoList2 설정

        when(teamScheduleMapper.scheduleListToTeamScheduleRspDtos(scheduleList1)).thenReturn(teamScheduleRspDtoList1);
        when(teamScheduleMapper.scheduleListToTeamScheduleRspDtos(scheduleList2)).thenReturn(teamScheduleRspDtoList2);

        // 테스트 실행
        List<List<TeamScheduleRspDto>> result = teamService.findTeamSchedule(teamScheduleListReqDto, userId);

        // 결과 검증
        assertEquals(2, result.size());
        // 기타 검증
    }
}
