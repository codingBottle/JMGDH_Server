package com.codingbottle.calendar.domain.daterepeater;

import com.codingbottle.calendar.domain.schedule.entity.Schedule;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ScheduleRepeater {
    static final int EVERY_WEEK = 1;
    static final int EVERY_TWO_WEEKS = 2;
    static final int EVERY_FOUR_WEEKS = 4;

    public static List<Schedule> repeat(Schedule scheduleToRepeat, RepeatInterval repeatInterval, int repeatCount) {
        if (repeatCount < 0) throw new IllegalArgumentException("반복횟수는 0보다 작을 수 없습니다.");

        return switch (repeatInterval) {
            case EVERY_WEEK -> repeatByWeek(scheduleToRepeat, EVERY_WEEK,  repeatCount);
            case EVERY_TWO_WEEKS -> repeatByWeek(scheduleToRepeat, EVERY_TWO_WEEKS,  repeatCount);
            case EVERY_FOUR_WEEKS -> repeatByWeek(scheduleToRepeat, EVERY_FOUR_WEEKS,  repeatCount);
            case EVERY_DAY -> repeatByEveryDay(scheduleToRepeat, repeatCount);
            default -> throw new IllegalArgumentException("지원하지 않는 반복구간입니다.");
        };
    }

    private static List<Schedule> repeatByWeek(Schedule scheduleToRepeat, int weekInterval, int repeatCount) {
        String repeatCode = UUID.randomUUID().toString();
        return IntStream.rangeClosed(0, repeatCount)
                .mapToObj(i -> Schedule.builder()
                        .member(scheduleToRepeat.getMember())
                        .title(scheduleToRepeat.getTitle())
                        .startDate(scheduleToRepeat.getStartDate().plusWeeks((long) weekInterval * i))
                        .endDate(scheduleToRepeat.getEndDate().plusWeeks((long) weekInterval * i))
                        .timeOfStartDate(scheduleToRepeat.getTimeOfStartDate())
                        .timeOfEndDate(scheduleToRepeat.getTimeOfEndDate())
                        .isAllDay(scheduleToRepeat.isAllDay())
                        .colorCode(scheduleToRepeat.getColorCode())
                        .isRepeat(true)
                        .repeatCode(repeatCode)
                        .build())
                .toList();
    }

    private static List<Schedule> repeatByEveryDay(Schedule scheduleToRepeat, int repeatCount) {
        String repeatCode = UUID.randomUUID().toString();
        return IntStream.rangeClosed(0, repeatCount)
                .mapToObj(i -> Schedule.builder()
                        .member(scheduleToRepeat.getMember())
                        .title(scheduleToRepeat.getTitle())
                        .startDate(scheduleToRepeat.getStartDate().plusDays(i))
                        .endDate(scheduleToRepeat.getEndDate().plusDays(i))
                        .timeOfStartDate(scheduleToRepeat.getTimeOfStartDate())
                        .timeOfEndDate(scheduleToRepeat.getTimeOfEndDate())
                        .isAllDay(scheduleToRepeat.isAllDay())
                        .isRepeat(true)
                        .repeatCode(repeatCode)
                        .build())
                .toList();
    }
}



