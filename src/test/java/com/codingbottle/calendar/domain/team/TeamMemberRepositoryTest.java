package com.codingbottle.calendar.domain.team;

import com.codingbottle.calendar.domain.team.entity.Team;
import com.codingbottle.calendar.domain.team.entity.TeamCode;
import com.codingbottle.calendar.domain.team.entity.TeamMember;
import com.codingbottle.calendar.domain.team.repository.TeamMemberRepository;
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
public class TeamMemberRepositoryTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;
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

        TeamMember teamMember = TeamMember.builder()
                .id(1L)
                .team(team)
                .member(member)
                .build();


        memberRepository.save(member);
        teamRepository.save(team);
        teamMemberRepository.save(teamMember);


        Optional<TeamMember> teamMemberList1 = teamMemberRepository.findTeamMemberListByMember_IdAndTeam_Id(1L, 1L);

        assertTrue(teamMemberList1.isPresent());

        TeamMember teamMember2 = teamMemberList1.get();

        System.out.println(teamMember2.getMember().getEmail());
        System.out.println(teamMember.getMember().getEmail());

        assertTrue(teamMember.getMember().getEmail().equals(teamMember2.getMember().getEmail()));
        assertTrue(teamMember.getTeam().getId().equals(teamMember2.getTeam().getId()));
    }
}
