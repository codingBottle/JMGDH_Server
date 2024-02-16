package com.codingbottle.calendar.domain.team.entity;

import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamMemberList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 우선순위 컬럼 추가해도 좋을 듯?

    @Builder
    protected TeamMemberList(Long id, Team team, Member member) {
        this.id = id;
        this.team = team;
        this.member = member;
    }
}
