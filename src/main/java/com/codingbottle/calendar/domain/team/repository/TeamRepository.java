package com.codingbottle.calendar.domain.team.repository;

import com.codingbottle.calendar.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t from Team t JOIN FETCH t.teamCode JOIN FETCH  t.leader JOIN FETCH t.teamMembers WHERE t.id = :teamId")
    Optional<Team> findById(@Param("teamId") Long teamId);

    @Query("SELECT t from Team t JOIN FETCH t.teamCode JOIN FETCH  t.leader JOIN FETCH t.teamMembers WHERE t.teamCode.code = :teamCode")
    Optional<Team> findTeamByTeamCode(@Param("teamCode") String teamCode);
}
