package com.codingbottle.calendar.domain.friend.service;

import com.codingbottle.calendar.domain.friend.dto.FriendReqDto;
import com.codingbottle.calendar.domain.friend.entity.Friend;
import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.friend.entity.FriendshipStatus;
import com.codingbottle.calendar.domain.friend.repository.FriendRepository;
import com.codingbottle.calendar.domain.friend.repository.FriendRequestRepository;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final MemberService memberService;

    //  친구 요청
    @Transactional
    public void sendFriendRequest(FriendReqDto reqDto, long memberId) {
        // 요청하는 멤버
        Member reqMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("요청자를 찾을 수 없습니다."));

        // 요청 받은 멤버
        Member rspMember = memberRepository.findByEmail(reqDto.rspMember())
                .orElseThrow(() -> new NoSuchElementException("응답자를 찾을 수 없습니다."));

        // 기존 요청이 있는지 확인
        FriendRequest friendRequest = friendRequestRepository.findByReqMemberAndRspMember(reqMember, rspMember)
                .orElse(null);

        // 거절 후 친구 재요청일 경우 상태 업데이트
        if (friendRequest != null && friendRequest.getStatus() == FriendshipStatus.DENY) {
            friendRequest.pendingStatus();
        }

        // 기존 요청이 없는 경우 새로운 요청 생성 (자기 자신에겐 친구 요청 불가)
        // 대기 중인 요청이 있는데 뒤바뀐 요청이 들어올 경우에도 새로운 요청 생성
        else if (friendRequest == null && !reqMember.equals(rspMember)) {
            friendRequest = new FriendRequest(reqMember, rspMember, FriendshipStatus.PENDING);
            // 요청자와 응답자가 뒤바뀐 경우 기존에 있던 요청을 삭제
            friendRequestRepository.findByReqMemberAndRspMember(rspMember, reqMember).ifPresent(friendRequestRepository::delete);
        }

        // 대기 중인 기존 요청이 있거나 요청자와 응답자가 같은 경우에는 예외 처리
        else {
            throw new IllegalStateException("친구 요청이 이미 존재하거나 자기 자신에겐 요청을 보낼 수 없습니다.");
        }

        // 요청자와 회원 정보가 일치하지 않으면 예외
        Member member = friendRequest.getReqMember();
        if(!member.getId().equals(reqMember.getId())) {
            throw new IllegalStateException("회원 정보가 일치하지 않습니다.");
        }

        // 친구 요청 저장
        friendRequestRepository.save(friendRequest);

    }

    // 친구 요청 수락
    @Transactional
    public void acceptFriendRequest(FriendReqDto reqDto) {
        // 요청 보낸 멤버
        Member reqMember = memberRepository.findByEmail(reqDto.reqMember())
                .orElseThrow(() -> new NoSuchElementException("요청자를 찾을 수 없습니다."));

        // 요청 받은 멤버
        Member rspMember = memberRepository.findByEmail(reqDto.rspMember())
                .orElseThrow(() -> new NoSuchElementException("응답자를 찾을 수 없습니다."));

        // 친구 요청을 찾을 수 없는 경우 예외 처리
        FriendRequest friendRequest = friendRequestRepository.findByReqMemberAndRspMember(reqMember, rspMember)
                .orElseThrow(() -> new NoSuchElementException("친구 요청을 찾을 수 없습니다."));

        // 친구 관계를 생성하여 저장
        Friend friend1 = new Friend(reqMember, rspMember, FriendshipStatus.ACCEPT);
        Friend friend2 = new Friend(rspMember, reqMember, FriendshipStatus.ACCEPT);
        friendRepository.saveAll(List.of(friend1, friend2));

        // 친구 요청 목록에서 삭제
        friendRequestRepository.delete(friendRequest);
    }

    // 친구 요청 거절
    @Transactional
    public void rejectFriendRequest(FriendReqDto reqDto, long memberId) {
        // 요청 보낸 멤버
        Member reqMember = memberRepository.findByEmail(reqDto.reqMember())
                .orElseThrow(() -> new NoSuchElementException("요청자를 찾을 수 없습니다."));

        // 요청 받은 멤버
        Member rspMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("응답자를 찾을 수 없습니다."));

        // 요청 목록을 찾을 수 없는 경우 예외
        FriendRequest friendRequest = friendRequestRepository.findByReqMemberAndRspMember(reqMember, rspMember)
                .orElseThrow(() -> new NoSuchElementException("친구 요청을 찾을 수 없습니다."));

        // 요청 받은 자와 회원 정보가 일치하지 않으면 예외
        Member member = friendRequest.getRspMember();
        if(!member.getId().equals(rspMember.getId())) {
            throw new IllegalStateException("회원 정보가 일치하지 않습니다.");
        }

        // 상태 업데이트
        friendRequest.denyStatus();
        friendRequestRepository.save(friendRequest);
    }

    // 친추 요청 목록 조회
    public List<FriendRequest> getRequestList(long memberId) {
        // 회원이 없는 경우 예외
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        return friendRequestRepository.findFriendRequestsByRspMember(member);
    }

    // 친구 목록 조회
    public List<Friend> getFriendList(long memberId) {
        // 회원이 없는 경우 예외
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        return friendRepository.findFriendsByMember(member);
    }

    // 친구 삭제
    @Transactional
    public void removeFriend(long memberId, String friendEmail) {
        // 본인 정보
        Member member1 = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("본인의 정보를 찾을 수 없습니다."));

        // 친구 정보
        Member member2 = memberRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new NoSuchElementException("친구의 정보를 찾을 수 없습니다."));

        // 본인을 삭제하려는 경우 예외 처리
        if (member1.getEmail().equals(friendEmail)) {
            throw new IllegalArgumentException("본인을 친구 목록에서 삭제할 수 없습니다.");
        }

        // 친구 목록을 찾을 수 없는 경우 예외
        Friend friend = friendRepository.findReverseFriends(member1, member2)
                .orElseThrow(() -> new NoSuchElementException("친구 목록을 찾을 수 없습니다."));

        friendRepository.delete(friend);
        removeReverseFriend(member1, member2);
        removeReverseFriend(member2, member1);
    }

    // 양방향 친구 삭제
    @Transactional
    public void removeReverseFriend(Member member1, Member member2) {
        Optional<Friend> reverseFriend = friendRepository.findReverseFriends(member1, member2);
        reverseFriend.ifPresent(friendRepository::delete);
    }
}
