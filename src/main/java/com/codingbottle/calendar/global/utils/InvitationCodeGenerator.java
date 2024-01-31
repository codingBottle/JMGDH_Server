package com.codingbottle.calendar.global.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class InvitationCodeGenerator {
    // 랜덤 초대 코드 생성
    public String generateInvitationCode() {
        int codeLength = 8; // 초대 코드 길이
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}
