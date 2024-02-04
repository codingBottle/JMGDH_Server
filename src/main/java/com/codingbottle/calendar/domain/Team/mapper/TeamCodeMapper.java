package com.codingbottle.calendar.domain.Team.mapper;

import com.codingbottle.calendar.domain.Team.dto.TeamCodeRspDto;
import com.codingbottle.calendar.domain.Team.entity.TeamCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamCodeMapper {
    @Mapping(source = "team.id", target = "teamId")
    TeamCodeRspDto teamCodeToTeamCodeRspDto(TeamCode teamCode);
}
