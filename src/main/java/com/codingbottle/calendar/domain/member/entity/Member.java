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
    private Long id;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 50, unique = true, updatable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    @Builder
    public Member(Long id, String nickname, String email, List<String> role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}