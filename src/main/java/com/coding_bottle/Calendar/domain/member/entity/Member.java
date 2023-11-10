package com.coding_bottle.Calendar.domain.member.entity;

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
public class Member {
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

    @Builder
    private Member(String nickname, String email, String password, List<String> role) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Builder
    protected Member(String nickname, String email, String password, Long memberId, List<String> role) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.memberId = memberId;
        this.role = role;
    }
}