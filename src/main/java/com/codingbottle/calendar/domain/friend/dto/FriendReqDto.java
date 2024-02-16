package com.codingbottle.calendar.domain.friend.dto;

import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.friend.entity.FriendshipStatus;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;

import javax.validation.constraints.NotBlank;
import java.util.NoSuchElementException;

public record FriendReqDto(
        @NotBlank(message = "요청자를 입력해주세요.")
        String reqMember,
        @NotBlank(message = "응답자를 입력해주세요.")
        String rspMember,

        FriendshipStatus status
) {
    public FriendRequest toFriendRequest(MemberRepository memberRepository) {
        // 요청자 정보
        Member reqMemberEntity = memberRepository.findByEmail(reqMember)
                .orElseThrow(() -> new NoSuchElementException("요청자를 찾을 수 없습니다."));

        // 응답자 정보
        Member rspMemberEntity = memberRepository.findByEmail(rspMember)
                .orElseThrow(() -> new NoSuchElementException("응답자를 찾을 수 없습니다."));

        return new FriendRequest(reqMemberEntity, rspMemberEntity, status);
    }
}
