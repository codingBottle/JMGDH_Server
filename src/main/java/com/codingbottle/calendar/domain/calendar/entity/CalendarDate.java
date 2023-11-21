package com.codingbottle.calendar.domain.calendar.entity;

import com.codingbottle.calendar.domain.common.BaseEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CalendarDate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;
    private int month;
    private int day;

    // 특정 달에 해당하는 유저 일정 조회 시 where user_id =... AND year =... AND month=... 로 조회한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public CalendarDate(LocalDate date, Member member) {
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
        this.member = member;
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(year, month, day);
    }
}