package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
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
			vacancyAnalytics.add(sortMap(vacancyAnalytic));
		}
//		Optional <VacancyAnalytic> vacancyAnalytic =vacancyAnalyticRepository.findOne();
//		if (vacancyAnalytic.isPresent()){
//			System.out.println("1111");
//		}else {
//			System.out.println("222");
//		}

		return vacancyAnalytics;
	}

	public VacancyAnalytic getAnalyticByName (String mainSkill, String cityName) throws ParseException, InterruptedException, IOException {
		String decode = URLDecoder.decode(mainSkill, "UTF-8");
//		vacancyAnalyticRepository.save(sortMap(hhVacanciesService.getVacancies(decode, cityName)));

		//return  sortMap(hhVacanciesService.getVacancies(decode, cityName));
		return  sortMap(hhVacanciesService.getAnalytic(decode, cityName));
	}


	public List<VacancyAnalytic> getAnalyticListByNameAndCity(String mainSkill, String cityName){
		log.debug("getAnalyticListByNameAndCity: mainSkill = {}, cityName = {}", mainSkill, cityName);
		return vacancyAnalyticRepository.findAllByMainSkillAndCity(mainSkill, cityName);
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
					.city(vacancyAnalytic.getCity())
					.build();
	}


}
