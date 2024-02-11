package com.codingbottle.calendar.domain.team;

import com.codingbottle.calendar.domain.team.entity.Team;
import com.codingbottle.calendar.domain.team.entity.TeamCode;
import com.codingbottle.calendar.domain.team.entity.TeamMemberList;
import com.codingbottle.calendar.domain.team.repository.TeamMemberListRepository;
import com.codingbottle.calendar.domain.team.repository.TeamRepository;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // DataJpaTest의 기본 액세스가 h2로 되어있어서 이렇게 설정해야 mysql로 연결됨
public class TeamMemberListRepositoryTest {

    @Autowired
    private TeamMemberListRepository teamMemberListRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void test1() {
        Member member = Member.builder()
                .id(1L)
                .nickname("테스트용")
                .email("yjy1219")
                .role(List.of("USER"))
                .build();

        //(1L, "ex3134dfe", LocalDateTime.now());
        TeamCode teamCode = TeamCode.builder()
                .id(1L)
                .code("ex3134dfe")
                .expirationTime(LocalDateTime.now())
                .build();

        Team team = Team.builder()
                .id(1L)
                .teamCode(teamCode)
                .leader(member)
                .name("먹동")
                .build();

        TeamMemberList teamMemberList = TeamMemberList.builder()
                .id(1L)
                .team(team)
                .member(member)
                .build();


        memberRepository.save(member);
        teamRepository.save(team);
        teamMemberListRepository.save(teamMemberList);


        Optional<TeamMemberList> teamMemberList1 = teamMemberListRepository.findTeamMemberListByMember_IdAndTeam_Id(1L, 1L);

        assertTrue(teamMemberList1.isPresent());

        TeamMemberList teamMemberList2 = teamMemberList1.get();

        System.out.println(teamMemberList2.getMember().getEmail());
        System.out.println(teamMemberList.getMember().getEmail());

        assertTrue(teamMemberList.getMember().getEmail().equals(teamMemberList2.getMember().getEmail()));
        assertTrue(teamMemberList.getTeam().getId().equals(teamMemberList2.getTeam().getId()));
    }
}
