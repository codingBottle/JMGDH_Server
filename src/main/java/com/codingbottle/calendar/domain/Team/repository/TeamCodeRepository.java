package com.codingbottle.calendar.domain.Team.repository;

import com.codingbottle.calendar.domain.Team.entity.TeamCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamCodeRepository extends JpaRepository<TeamCode, Long> {
    @Query("SELECT t FROM TeamCode t JOIN FETCH t.team WHERE t.team.id = :teamId")
    TeamCode findTeamCodeByTeam_Id(@Param("teamId") Long teamId);
}
