package com.codingbottle.calendar.domain.Team.repository;

import com.codingbottle.calendar.domain.Team.entity.TeamMemberList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMemberListRepository extends JpaRepository<TeamMemberList, Long> {
    List<TeamMemberList> findTeamMemberListByMember_Id(Long memberId);
    @Query("SELECT t FROM TeamMemberList t " +
            "JOIN FETCH t.team JOIN FETCH t.member " +
            "WHERE t.team.id = :teamId AND t.member.email LIKE CONCAT(:email, '%')")
    List<TeamMemberList> findTeamMemberByTeamIdAndEmail(@Param("teamId") Long teamId, @Param("email") String email);
    Optional<TeamMemberList> findTeamMemberListByMember_IdAndTeam_Id(Long memberId, Long teamId);
}
