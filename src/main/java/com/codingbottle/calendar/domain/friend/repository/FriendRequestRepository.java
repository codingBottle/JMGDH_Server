package com.codingbottle.calendar.domain.friend.repository;

import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // 이미 친구 요청이 있는 경우를 위함
    Optional<FriendRequest> findByReqMemberAndRspMember(Member reqMember, Member rspMember);

    // 응답자 기준으로 요청 목록 조회
    @Query("SELECT DISTINCT f FROM FriendRequest f WHERE (f.rspMember = :rspMember)")
    List<FriendRequest> findFriendRequestsByRspMember(@Param("rspMember") Member rspMember);
}