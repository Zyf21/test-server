package com.spring.server.testserver.repositories;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.domain.vacancy.VacancyDataDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyDataRepository extends JpaRepository<VacancyData, Long>  {

//	@Query(
//			value = "SELECT  name,city,company_name FROM vacancies;",
//			nativeQuery = true)
//	List<VacancyData> getcity();

	List<VacancyData> findByCompanyName(String name);
}
