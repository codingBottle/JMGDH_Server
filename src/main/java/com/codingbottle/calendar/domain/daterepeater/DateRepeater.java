package com.codingbottle.calendar.domain.daterepeater;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DateRepeater {
    static final int EVERY_WEEK = 1;
    static final int EVERY_TWO_WEEKS = 2;
    static final int EVERY_FOUR_WEEKS = 4;

    public static List<LocalDate> repeat(LocalDate startDate, RepeatInterval repeatInterval, int repeatCount) {
        // repeatInterval에 따라 startDate부터 repeatCount만큼 반복한 날짜들을 List에 담아 반환한다.
        return switch (repeatInterval) {
            case EVERY_WEEK -> repeatByWeek(startDate, EVERY_WEEK,  repeatCount);
            case EVERY_TWO_WEEKS -> repeatByWeek(startDate, EVERY_TWO_WEEKS,  repeatCount);
            case EVERY_FOUR_WEEKS -> repeatByWeek(startDate, EVERY_FOUR_WEEKS,  repeatCount);
            default -> throw new IllegalArgumentException("지원하지 않는 반복구간입니다.");
        };
    }

    private static List<LocalDate> repeatByWeek(LocalDate startDate, int weekInterval, int repeatCount) {
        return IntStream.rangeClosed(0, repeatCount)
                .mapToObj(i -> startDate.plusWeeks(weekInterval * i))
                .toList();
    }


}
