package com.spring.server.testserver.repositories;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacancyAnalyticRepository extends JpaRepository<VacancyAnalytic, Long> {

    List<VacancyAnalytic> findAllByMainSkillAndCity(String mainSkill, String cityName);


}
