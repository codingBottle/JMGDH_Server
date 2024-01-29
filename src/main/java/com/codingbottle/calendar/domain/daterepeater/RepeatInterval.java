package com.codingbottle.calendar.domain.daterepeater;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RepeatInterval {
    EVERY_DAY("매일") ,EVERY_WEEK("매주마다"), EVERY_TWO_WEEKS("격주마다")
    , EVERY_FOUR_WEEKS("4주마다");

    public final String description;
}
