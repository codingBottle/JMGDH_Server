package com.codingbottle.calendar.domain.friend.repository;

import com.codingbottle.calendar.domain.friend.entity.Friend;
import com.codingbottle.calendar.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 친구 조회 시 member1을 기준으로 member2만 출력
    @Query("SELECT DISTINCT f FROM Friend f " +
            "WHERE (f.member1 = :member)")
    List<Friend> findFriendsByMember(@Param("member") Member member);

    // 친구 삭제 (컬럼 2개 삭제용)
    @Query("SELECT f FROM Friend f WHERE f.member1 = :member1 AND f.member2 = :member2")
    Optional<Friend> findReverseFriends(Member member1, Member member2);
}