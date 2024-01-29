package com.codingbottle.calendar.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColorUtil {
    public static final String SCHEDULE_DEFAULT_COLOR_CODE = "EFE1E1";
    public static boolean isColorCode(String str) {
        if (! StringUtils.hasText(str)) {
            return false;
        }
        return str.matches("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }
}
