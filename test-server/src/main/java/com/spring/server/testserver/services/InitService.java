package com.spring.server.testserver.services;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class InitService {

	private final HHVacanciesService hhVacanciesService;

	public InitService(final HHVacanciesService hhVacanciesService) {
		this.hhVacanciesService = hhVacanciesService;
	}

	@PostConstruct
	public void init() throws IOException, JAXBException, ParseException {

		hhVacanciesService.getVacancies();
	}

}
