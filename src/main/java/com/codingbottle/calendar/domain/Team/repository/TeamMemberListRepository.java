package com.codingbottle.calendar.domain.Team.repository;

import com.codingbottle.calendar.domain.Team.entity.TeamMemberList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberListRepository extends JpaRepository<TeamMemberList, Long> {
    List<TeamMemberList> findTeamMemberListByMember_Id(Long memberId);
}
