package com.spring.server.testserver.repositories;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyAnalyticRepository extends JpaRepository<VacancyAnalytic, Long> {
}
