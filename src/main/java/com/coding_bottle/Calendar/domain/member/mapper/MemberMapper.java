package com.coding_bottle.Calendar.domain.member.mapper;

import com.coding_bottle.Calendar.domain.member.dto.MemberResponseDto;
import com.coding_bottle.Calendar.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberResponseDto memberToMemberResponseDto(Member member);
}