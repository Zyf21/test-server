package com.spring.server.testserver.controllers;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.services.VacancyAnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacancy/analytic")
public class VacancyAnalyticController {

	private final VacancyAnalyticService vacancyAnalyticService;

	public VacancyAnalyticController(VacancyAnalyticService vacancyAnalyticService) {
		this.vacancyAnalyticService = vacancyAnalyticService;
	}


	@GetMapping("/vacancies")
	public List<VacancyAnalytic> getAnalytics(){
		return vacancyAnalyticService.getAnalytic();
	}
}
