package com.codingbottle.calendar.domain.utils;

import com.codingbottle.calendar.global.utils.Rfc3339DateConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class Rfc3339DateConverterTest {
    @Test
    void test1() {
        String date = Rfc3339DateConverter.convertNowLocalDateToRfc3339(LocalDate.now());
        System.out.println(date);
    }
}
