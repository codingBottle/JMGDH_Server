package com.codingbottle.calendar.domain.daterepeater;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DateRepeaterTest {

    @Test
    void 날짜를_반복간격_기준으로_반복횟수만큼_더하여_반환할_수_있다() {
        //given
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        int repeatCount = 3;

        List<LocalDate> expectedDatesWithEveryWeeks = Arrays.asList(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 8),
                LocalDate.of(2021, 1, 15)
        );

        List<LocalDate> expectedDatesWithEveryTwoWeeks = Arrays.asList(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 15),
                LocalDate.of(2021, 1, 29)
        );

        List<LocalDate> expectedDatesWithEveryFourWeeks = Arrays.asList(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 29),
                LocalDate.of(2021, 2, 26)
        );

        //when
        List<LocalDate> resultsEveryWeeks = DateRepeater.repeat(startDate, RepeatInterval.EVERY_WEEK, repeatCount);
        List<LocalDate> resultsTwoWeeks = DateRepeater.repeat(startDate, RepeatInterval.EVERY_TWO_WEEKS, repeatCount);
        List<LocalDate> resultsFourWeeks = DateRepeater.repeat(startDate, RepeatInterval.EVERY_FOUR_WEEKS, repeatCount);

        //then
        assertThat(resultsEveryWeeks).isEqualTo(expectedDatesWithEveryWeeks);
        assertThat(resultsTwoWeeks).isEqualTo(expectedDatesWithEveryTwoWeeks);
        assertThat(resultsFourWeeks).isEqualTo(expectedDatesWithEveryFourWeeks);
    }

}