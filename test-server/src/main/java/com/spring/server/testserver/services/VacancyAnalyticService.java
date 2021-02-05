package com.spring.server.testserver.services;

import com.spring.server.testserver.controllers.VacancyAnalyticController;
import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacancyAnalyticService {

	private final VacancyAnalyticRepository vacancyAnalyticRepository;

	public VacancyAnalyticService(VacancyAnalyticRepository vacancyAnalyticRepository) {
		this.vacancyAnalyticRepository = vacancyAnalyticRepository;
	}

	public List<VacancyAnalytic> getAnalytic() {
		return sortMap(vacancyAnalyticRepository.findAll());
	}

	public List<VacancyAnalytic> sortMap (List<VacancyAnalytic> vacancyAnalyticList) {

		List<VacancyAnalytic> newList = new ArrayList<>();
		for (VacancyAnalytic vacancyAnalytic : vacancyAnalyticList) {


			Map<String, Long> sortedlistSkills = vacancyAnalytic.getSkills().entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new));


			newList.add(VacancyAnalytic.builder()
					.vacancyCount(vacancyAnalytic.getVacancyCount())
					.levels(vacancyAnalytic.getLevels())
					.createAt(vacancyAnalytic.getCreateAt())
					.skills(sortedlistSkills)
					.build());

		}
		return newList;


	}
}
