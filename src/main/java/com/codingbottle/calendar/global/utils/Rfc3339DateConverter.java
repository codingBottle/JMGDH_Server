package com.codingbottle.calendar.global.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Rfc3339DateConverter {

    public static String convertNowLocalDateToRfc3339(LocalDate date) {

        // RFC3339 형식으로 변환하기 위해 DateTimeFormatter를 사용합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        // LocalDate를 RFC3339 형식의 문자열로 변환합니다.
        String rfc3339Date = date.atStartOfDay(ZoneId.of("Asia/Seoul")).format(formatter);

        System.out.println("RFC3339 형식으로 변환된 날짜: " + rfc3339Date);

        return rfc3339Date;
    }
}
