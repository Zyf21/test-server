package com.spring.server.testserver.controllers;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.services.VacancyAnalyticService;
import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@Log4j2
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

	@GetMapping("/vacancy")
	public VacancyAnalytic getAnalytic(@RequestParam String mainSkill, String cityName) throws ParseException, InterruptedException, IOException {
		log.debug("getVacancies: mainSkill = {}, cityName = {}", mainSkill, cityName);
		return vacancyAnalyticService.getAnalyticByName(mainSkill, cityName);
	}

	@GetMapping("/vacancy/list")
	public List<VacancyAnalytic> getAnalyticList(@RequestParam String mainSkill, String cityName){
		log.debug("getVacancies: mainSkill = {}, cityName = {}", mainSkill, cityName);
		return vacancyAnalyticService.getAnalyticListByNameAndCity(mainSkill, cityName);

	}

}
