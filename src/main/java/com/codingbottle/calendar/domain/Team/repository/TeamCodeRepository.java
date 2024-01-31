package com.codingbottle.calendar.domain.Team.repository;

import com.codingbottle.calendar.domain.Team.entity.TeamCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamCodeRepository extends JpaRepository<TeamCode, Long> {
}
