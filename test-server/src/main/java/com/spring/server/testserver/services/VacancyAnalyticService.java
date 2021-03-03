package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacancyAnalyticService {

	private final VacancyAnalyticRepository vacancyAnalyticRepository;
	private final HHVacanciesService hhVacanciesService;

	@Autowired
	public VacancyAnalyticService(VacancyAnalyticRepository vacancyAnalyticRepository,
								  HHVacanciesService hhVacanciesService) {
		this.vacancyAnalyticRepository = vacancyAnalyticRepository;
		this.hhVacanciesService = hhVacanciesService;
	}

	public List<VacancyAnalytic> getAnalytic() {

		List<VacancyAnalytic> vacancyAnalytics = new ArrayList<>();
		for (VacancyAnalytic vacancyAnalytic : vacancyAnalyticRepository.findAll()) {
			sortMap(vacancyAnalytic);
		}
		return vacancyAnalytics;
	}

	public VacancyAnalytic getAnalyticByName (String mainSkill, String cityName) throws ParseException, InterruptedException, IOException {
		String decode = URLDecoder.decode(mainSkill, "UTF-8");

		return  sortMap(hhVacanciesService.getVacancies(decode, cityName));
	}

	public VacancyAnalytic sortMap (VacancyAnalytic vacancyAnalytic) {

			Map<String, Long> sortedlistSkills = vacancyAnalytic.getSkills().entrySet()
					.stream()
					.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new));

			return VacancyAnalytic.builder()
					.id(vacancyAnalytic.getId())
					.mainSkill(vacancyAnalytic.getMainSkill())
					.vacancyCount(vacancyAnalytic.getVacancyCount())
					.levels(vacancyAnalytic.getLevels())
					.createAt(vacancyAnalytic.getCreateAt())
					.skills(sortedlistSkills)
					.build();
	}


}
