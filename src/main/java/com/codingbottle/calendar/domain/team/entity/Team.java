package com.codingbottle.calendar.domain.team.entity;

import com.codingbottle.calendar.domain.team.dto.TeamUpdateReqDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, optional = false)
    private TeamCode teamCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member leader;

    // 팀원 리스트
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMemberList> teamMemberLists = new ArrayList<>();

    // 팀 캘린더(제작 예정)

    @Builder
    protected Team(Long id, String name, TeamCode teamCode, Member leader, List<TeamMemberList> teamMemberLists) {
        this.id = id;
        this.name = name;
        this.teamCode = teamCode;
        this.leader = leader;
        this.teamMemberLists = teamMemberLists;
    }

    public void updateForm(TeamUpdateReqDto teamUpdateReqDto) {
        this.name = teamUpdateReqDto.name();
    }
    public void addTeamMemberLists(TeamMemberList teamMemberList) { this.teamMemberLists.add(teamMemberList); }
    public void setTeamCode(TeamCode teamCode) { this.teamCode = teamCode; }

}
