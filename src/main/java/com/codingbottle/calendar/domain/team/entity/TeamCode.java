package com.codingbottle.calendar.domain.team.entity;

import com.codingbottle.calendar.global.utils.InvitationCodeGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public TeamCode(Long id, String code, LocalDateTime expirationTime, Team team) {
        this.id = id;
        this.code = code;
        this.expirationTime = expirationTime;
        this.team = team;
    }

    public void generateTeamCode() {
        this.code = InvitationCodeGenerator.generateInvitationCode();
        this.expirationTime = LocalDateTime.now().plusMinutes(10);
    }
}
