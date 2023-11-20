package com.codingbottle.calendar.domain.member.mapper;

import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberResponseDto memberToMemberResponseDto(Member member);
}