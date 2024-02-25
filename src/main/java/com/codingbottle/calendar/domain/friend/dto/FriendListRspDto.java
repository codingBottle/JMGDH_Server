package com.codingbottle.calendar.domain.friend.dto;

import com.codingbottle.calendar.domain.friend.entity.Friend;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FriendListRspDto {

    List<FriendDto> friends;

    public static FriendListRspDto from(List<Friend> friends) {
        return new FriendListRspDto(FriendDto.from(friends));
    }

    @Getter
    static class FriendDto {
        long id;
        MemberDto member;

        public FriendDto(Friend friend) {
            this.id = friend.getId();
            this.member = new MemberDto(friend.getMember2());
        }

        static List<FriendDto> from(List<Friend> friends) {
            return friends.stream()
                    .map(FriendDto::new)
                    .toList();
        }

        // 친구의 nickname과 email만 출력
        @Getter
        @AllArgsConstructor
        static class MemberDto {
            String nickname;
            String email;
            @JsonProperty("profileImage")
            String imageUrl;

            public MemberDto(Member member) {
                this.nickname = member.getNickname();
                this.email = member.getEmail();
                this.imageUrl = member.getImageUrl();
            }
        }
    }
}
