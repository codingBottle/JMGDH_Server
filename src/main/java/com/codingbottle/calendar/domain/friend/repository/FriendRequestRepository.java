package com.codingbottle.calendar.domain.friend.repository;

import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // 이미 친구 요청이 있는 경우를 위함
    Optional<FriendRequest> findByReqMemberAndRspMember(Member reqMember, Member rspMember);
}
