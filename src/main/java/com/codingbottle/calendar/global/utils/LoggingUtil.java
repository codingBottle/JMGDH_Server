package com.codingbottle.calendar.global.utils;

import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LoggingUtil {

    // printStackTrace 호출 후 String으로 반환함
    public static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw); // 반환값용 로그 남기기
        return sw.toString();
    }
}
