package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.repositories.VacancyDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyDataService {
	
	private final VacancyDataRepository vacancyDataRepository;

	public VacancyDataService( final VacancyDataRepository vacancyDataRepository) {
		this.vacancyDataRepository = vacancyDataRepository;
	}


	public List<VacancyData> getVacancies (){
	//	VacancyData vacancyData = vacancyDataRepository.findAll().get(40);
	//	System.out.println(vacancyData);

		return vacancyDataRepository.findAll();
	}



}
