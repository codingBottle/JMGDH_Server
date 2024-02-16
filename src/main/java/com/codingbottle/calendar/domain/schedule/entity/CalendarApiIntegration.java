package com.codingbottle.calendar.domain.schedule.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CalendarApiIntegration extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String lastPageToken;

    @Column(nullable = true)
    private String lastEventId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public CalendarApiIntegration(Long id, String lastPageToken, String lastEventId, Member member) {
        this.id = id;
        this.lastPageToken = lastPageToken;
        this.lastEventId = lastEventId;
        this.member = member;
    }

    public CalendarApiIntegrationBuilder toBuilder() {
        return CalendarApiIntegration.builder()
                .id(this.id)
                .lastPageToken(this.lastPageToken)
                .lastEventId(this.lastEventId)
                .member(this.member);
    }
}
