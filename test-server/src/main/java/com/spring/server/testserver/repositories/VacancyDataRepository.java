package com.spring.server.testserver.repositories;

import com.spring.server.testserver.domain.vacancy.VacancyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyDataRepository extends JpaRepository<VacancyData, Long>  {
}
