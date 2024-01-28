package com.codingbottle.calendar.domain.schedule.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.global.utils.ColorUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = true)
    private LocalTime timeOfStartDate;
    @Column(nullable = true)
    private LocalTime timeOfEndDate;
    @Column(nullable = false)
    private boolean isAllDay;

    @Column(nullable = false)
    private String colorCode;

    @Column(nullable = false)
    private boolean isRepeat;
    @Column(nullable = true)
    private String repeatCode; // 반복 일정을 한꺼번에 삭제하기 위함. ex) 'delete ... where repeatCode = :?'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Schedule notAllDay(Member member, String title, LocalDate startDate, LocalDate endDate
            , LocalTime timeOfStartDate, LocalTime timeOfEndDate, String colorCode){
        return Schedule.builder()
                .member(member)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .timeOfStartDate(timeOfStartDate)
                .timeOfEndDate(timeOfEndDate)
                .isAllDay(false)
                .colorCode(colorCode)
                .isRepeat(false)
                .build();
    }

    public static Schedule allDay(Member member, String title, LocalDate startDate, LocalDate endDate, String colorCode){
        return Schedule.builder()
                .member(member)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .isAllDay(true)
                .colorCode(colorCode)
                .isRepeat(false)
                .build();
    }

    @Builder
    private Schedule(String title, LocalDate startDate, LocalDate endDate, LocalTime timeOfStartDate, LocalTime timeOfEndDate, boolean isAllDay, String colorCode, boolean isRepeat, String repeatCode, Member member) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeOfStartDate = timeOfStartDate;
        this.timeOfEndDate = timeOfEndDate;
        this.isAllDay = isAllDay;
        this.colorCode  = ColorUtil.isColorCode(colorCode) ? colorCode : ColorUtil.SCHEDULE_DEFAULT_COLOR_CODE; // 색상 미입력 시 기본 연분홍으로 결정됨
        this.isRepeat = isRepeat;
        this.repeatCode = repeatCode;
        this.member = member;
        if (this.isRepeat && this.repeatCode == null) {
            throw new IllegalStateException("반복코드 없이 반복일정을 생성할 수 없습니다.");
        }
        if (this.startDate.isAfter(this.endDate)) {
            throw new IllegalStateException("시작일이 종료일보다 늦을 수 없습니다.");
        }

        // 종일 일정이 아닌 경우에만 검사
        if (! this.isAllDay) {
            if (this.timeOfStartDate == null || this.timeOfEndDate == null) {
                throw new IllegalStateException("종일 일정이 아닌 경우 시간이 존재해야 합니다.");
            }
            LocalDateTime startDateTime = LocalDateTime.of(this.startDate, this.timeOfStartDate);
            LocalDateTime endDateTime = LocalDateTime.of(this.endDate, this.timeOfEndDate);
            if (startDateTime.isAfter(endDateTime)) {
                throw new IllegalStateException("시작시간이 종료시간보다 늦을 수 없습니다.");
            }
            // 두 시간의 차이는 24시간 이내여야 함
            if (startDateTime.plusHours(24).isBefore(endDateTime)) {
                throw new IllegalStateException("종일 일정이 아닐 경우 시작시간과 종료시간의 차이는 24시간 이내여야 합니다.");
            }
        }

        if (this.isAllDay &&
                (this.timeOfStartDate != null || this.timeOfEndDate != null)) {
            throw new IllegalStateException("종일 일정은 시간을 가질 수 없습니다.");
        }
    }

    //  update
    public void updateFrom(Schedule scheduleToUpdate) {
        this.title = scheduleToUpdate.title;
        this.startDate = scheduleToUpdate.startDate;
        this.endDate = scheduleToUpdate.endDate;
        this.timeOfStartDate = scheduleToUpdate.timeOfStartDate;
        this.timeOfEndDate = scheduleToUpdate.timeOfEndDate;
        this.isAllDay = scheduleToUpdate.isAllDay;
    }
}