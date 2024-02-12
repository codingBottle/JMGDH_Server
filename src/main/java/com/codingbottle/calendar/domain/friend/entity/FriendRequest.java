package com.codingbottle.calendar.domain.friend.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.friend.dto.FriendReqDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class FriendRequest extends BaseTimeEntity {    // 친구 요청 목록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  친구 요청을 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", nullable = false, referencedColumnName = "email")
    private Member reqMember;

    //  친구 요청을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient", nullable = false, referencedColumnName = "email")
    private Member rspMember;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public FriendRequest(Member reqMember, Member rspMember, FriendshipStatus status) {
        this.reqMember = reqMember;
        this.rspMember = rspMember;
        this.status = status;
    }

    //  친구 요청 상태 변경
    public void updateStatus(FriendReqDto reqDto) {
        this.status = reqDto.status();
    }
}
