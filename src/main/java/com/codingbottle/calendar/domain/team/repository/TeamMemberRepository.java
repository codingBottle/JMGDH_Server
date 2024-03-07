package com.codingbottle.calendar.domain.team.repository;

import com.codingbottle.calendar.domain.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findTeamMemberListByMember_Id(Long memberId);
    @Query("SELECT t FROM TeamMember t " +
            "JOIN FETCH t.team JOIN FETCH t.member " +
            "WHERE t.team.id = :teamId AND t.member.email LIKE CONCAT(:email, '%')")
    List<TeamMember> findTeamMemberByTeamIdAndEmail(@Param("teamId") Long teamId, @Param("email") String email);
    Optional<TeamMember> findTeamMemberListByMember_IdAndTeam_Id(Long memberId, Long teamId);
}
