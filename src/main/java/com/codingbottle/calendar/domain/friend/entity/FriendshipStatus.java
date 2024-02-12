package com.codingbottle.calendar.domain.friend.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FriendshipStatus {

    PENDING("대기 중"),
    ACCEPT("수락"),
    DENY("거절");

    public final String description;
}
