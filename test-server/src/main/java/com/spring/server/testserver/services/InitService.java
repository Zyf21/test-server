package com.spring.server.testserver.services;

import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Log4j2
@Service
public class InitService {

	private final HHVacanciesService hhVacanciesService;
	private final ReportHelper reportHelper;
	@Autowired
	public InitService(final HHVacanciesService hhVacanciesService, ReportHelper reportHelper) {
		this.hhVacanciesService = hhVacanciesService;
		this.reportHelper = reportHelper;
	}

	@PostConstruct
	public void init() throws IOException, ParseException, InterruptedException  {
		log.debug("2342342");
		log.getName();





		hhVacanciesService.getVacancies("Java","Беларусь");
//		String txtRootPath = "/home/dmtech/comreg/ruster1/";
//		String ascRootPath = "/home/dmtech/comreg/ruster3/"; // TODO change paths to ASC paths

//		reportHelper.createReport(txtRootPath, ascRootPath,"uyuyu");
	}

}
