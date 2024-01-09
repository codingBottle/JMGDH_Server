package com.codingbottle.calendar.global.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomPasswordGenerator {
    // 정규식 패턴
    private  final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

    // 랜덤 비밀번호 생성
    public  String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // 비밀번호 길이
        int length = 12;

        // 비밀번호에 포함될 문자들
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*#?&";

        // 정규식 패턴 확인
        while (!password.toString().matches(PASSWORD_PATTERN)) {
            password.setLength(0); // 이전에 생성한 비밀번호를 초기화

            // 랜덤하게 문자 선택하여 비밀번호 생성
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                password.append(characters.charAt(index));
            }
        }

        return password.toString();
    }
}