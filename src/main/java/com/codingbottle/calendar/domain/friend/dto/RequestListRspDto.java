package com.codingbottle.calendar.domain.friend.dto;

import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RequestListRspDto {

    List<RequestDto> requests;

    public static RequestListRspDto from(List<FriendRequest> requests) {
        return new RequestListRspDto(RequestDto.from(requests));
    }

    @Getter
    static class RequestDto {
        long id;
        MemberDto reqMember;

        public RequestDto(FriendRequest request) {
            this.id = request.getId();
            this.reqMember = new MemberDto(request.getReqMember());
        }

        static List<RequestDto> from(List<FriendRequest> requests) {
            return requests.stream()
                    .map(RequestDto::new)
                    .toList();
        }

        // 친구의 nickname과 email만 출력
        @Getter
        @AllArgsConstructor
        static class MemberDto {
            String nickname;
            String email;
            @JsonProperty("profileImage")
            String profileImageUrl;

            public MemberDto(Member member) {
                this.nickname = member.getNickname();
                this.email = member.getEmail();
                this.profileImageUrl = member.getImageUrl();
            }
        }
    }
}
