package com.codingbottle.calendar.domain.team.mapper;

import com.codingbottle.calendar.domain.team.dto.TeamCodeRspDto;
import com.codingbottle.calendar.domain.team.entity.TeamCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamCodeMapper {
    @Mapping(source = "team.id", target = "teamId")
    TeamCodeRspDto teamCodeToTeamCodeRspDto(TeamCode teamCode);
}
