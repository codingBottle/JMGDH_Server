package com.codingbottle.calendar.domain.friend.dto;

import javax.validation.constraints.NotBlank;

public record FriendReqDto(
        @NotBlank(message = "요청자를 입력해주세요.")
        String reqMember,
        @NotBlank(message = "응답자를 입력해주세요.")
        String rspMember
) {
}
