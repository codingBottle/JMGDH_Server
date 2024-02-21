package com.codingbottle.calendar.domain.friend.controller;

import com.codingbottle.calendar.domain.friend.dto.FriendListRspDto;
import com.codingbottle.calendar.domain.friend.dto.FriendReqDto;
import com.codingbottle.calendar.domain.friend.dto.RequestListRspDto;
import com.codingbottle.calendar.domain.friend.entity.Friend;
import com.codingbottle.calendar.domain.friend.entity.FriendRequest;
import com.codingbottle.calendar.domain.friend.service.FriendService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    // 친구 요청
    @PostMapping("/send-request")
    public ResponseEntity<RspTemplate<Void>> sendFriendRequest(@RequestBody @Validated FriendReqDto reqDto, Authentication authentication) {
        friendService.sendFriendRequest(reqDto, Long.parseLong(authentication.getName()));
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                reqDto.rspMember() + "님께 친구 요청을 보냈습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    // 요청 수락
    @PostMapping("/accept-request")
    public ResponseEntity<RspTemplate<Void>> acceptFriendRequest(@RequestBody FriendReqDto reqDto) {
        friendService.acceptFriendRequest(reqDto);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "친구 요청이 수락되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 요청 거절
    @PatchMapping("/reject-request")
    public ResponseEntity<RspTemplate<Void>> rejectFriendRequest(@RequestBody FriendReqDto reqDto, Authentication authentication) {
        friendService.rejectFriendRequest(reqDto, Long.parseLong(authentication.getName()));
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "친구 요청이 거절되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 친추 요청 목록 조회
    @GetMapping("/requests")
    public RspTemplate<RequestListRspDto> getRequestList(Authentication authentication) {
        List<FriendRequest> requests = friendService.getRequestList(Long.parseLong(authentication.getName()));
        RequestListRspDto rspDto = RequestListRspDto.from(requests);
        return new RspTemplate<>(HttpStatus.OK, "친추 요청 목록", rspDto);
    }

    // 친구 목록 조회
    @GetMapping
    public RspTemplate<FriendListRspDto> getFriendList(Authentication authentication) {
        List<Friend> friends = friendService.getFriendList(Long.parseLong(authentication.getName()));
        FriendListRspDto rspDto = FriendListRspDto.from(friends);
        return new RspTemplate<>(HttpStatus.OK, "친구 목록", rspDto);
    }

    // 친구 삭제
    @DeleteMapping("{friendEmail}")
    public ResponseEntity<RspTemplate<Void>> removeFriend(@PathVariable String friendEmail, Authentication authentication) {
        friendService.removeFriend(Long.parseLong(authentication.getName()), friendEmail);
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "친구가 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }
}