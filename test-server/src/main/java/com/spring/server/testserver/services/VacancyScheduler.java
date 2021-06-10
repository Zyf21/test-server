package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class VacancyScheduler {

    private static final String CRON_STR = "0 0 0 * * ?";

    private final HHVacanciesService hhVacanciesService;
    private final VacancyAnalyticRepository vacancyAnalyticRepository;

    public VacancyScheduler(final HHVacanciesService hhVacanciesService,
                            final VacancyAnalyticRepository vacancyAnalyticRepository) {
        this.hhVacanciesService = hhVacanciesService;
        this.vacancyAnalyticRepository = vacancyAnalyticRepository;

    }

    @Scheduled(cron = CRON_STR)
    public void checkVacancyAnalitic() {
        VacancyAnalytic vacancyAnalytic = hhVacanciesService.getAnalytic("Java","Беларусь");
        vacancyAnalyticRepository.save(vacancyAnalytic);
    }
}
