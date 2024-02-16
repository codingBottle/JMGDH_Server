package com.codingbottle.calendar.domain.member.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.schedule.entity.CalendarApiIntegration;
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

    @Column(nullable = false)
    private String imageUrl;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, optional = false)
    private CalendarApiIntegration calendarApiIntegration;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    @Builder
    public Member(Long id, String nickname, String email, String imageUrl, CalendarApiIntegration calendarApiIntegration, List<String> role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
        this.calendarApiIntegration = calendarApiIntegration;
        this.role = role;
    }

    public MemberBuilder toBuilder() {
        return Member.builder()
                .id(this.id)
                .nickname(this.nickname)
                .email(this.email)
                .imageUrl(this.imageUrl)
                .calendarApiIntegration(this.calendarApiIntegration)
                .role(this.role);
    }

    public void setCalendarApiIntegration(CalendarApiIntegration calendarApiIntegration) {
        this.calendarApiIntegration = calendarApiIntegration;
    }
}