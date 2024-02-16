package com.codingbottle.calendar.domain.friend.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseTimeEntity {   // 친구가 된 Entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1", nullable = false, referencedColumnName = "email")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2", nullable = false, referencedColumnName = "email")
    private Member member2;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friend(Member member1, Member member2, FriendshipStatus status) {
        this.member1 = member1;
        this.member2 = member2;
        this.status = status;
    }
}
