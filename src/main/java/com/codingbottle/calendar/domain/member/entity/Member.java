package com.codingbottle.calendar.domain.member.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
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
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 50, unique = true, updatable = false)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    @Column(nullable = false, length = 500)
    private String googleAccessToken;

    @Builder
    protected Member(String nickname, String email, String password, Long memberId, List<String> role, String googleAccessToken) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.googleAccessToken = googleAccessToken;
    }
}