package com.codingbottle.calendar.domain.Team.repository;

import com.codingbottle.calendar.domain.Team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
