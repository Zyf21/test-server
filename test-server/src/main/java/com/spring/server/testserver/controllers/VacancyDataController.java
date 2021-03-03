package com.spring.server.testserver.controllers;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.services.VacancyDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacancy/data")
public class VacancyDataController {

	private final VacancyDataService vacancyDataService;

	public VacancyDataController(VacancyDataService vacancyDataService) {
		this.vacancyDataService = vacancyDataService;
	}

	@GetMapping("/vacancies")
	public List<VacancyData> getVacancies(){
		return vacancyDataService.getVacancies();
	}
}
