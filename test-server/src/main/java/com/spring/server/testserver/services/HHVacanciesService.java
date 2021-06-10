package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.domain.vacancy.VacancyDTO;
import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import com.spring.server.testserver.repositories.VacancyDataRepository;
import com.zaxxer.hikari.util.ConcurrentBag;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Log4j2
@Service
public class HHVacanciesService {

	private final VacancyAnalyticRepository vacancyAnalyticRepository;
	private final VacancyDataRepository vacancyDataRepository;


	public HHVacanciesService(final VacancyAnalyticRepository vacancyAnalyticRepository,
							  final VacancyDataRepository vacancyDataRepository) {
		this.vacancyAnalyticRepository = vacancyAnalyticRepository;
		this.vacancyDataRepository = vacancyDataRepository;
	}


	public VacancyAnalytic getVacancies(String mainSkill, String cityName) throws IOException, ParseException, InterruptedException {
		log.debug("getVacancies: mainSkill = {}, cityName = {}", mainSkill, cityName);

		//List<String> vacantiesIds = new ArrayList<>();
		String cityId = getCityId(cityName);
		final Content getPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=" + cityId + "&search_field=name&per_page=100")
				.execute().returnContent();

		Object firstPage = new JSONParser().parse(getPage.asString());
		JSONObject firstPageJson = (JSONObject) firstPage;
		JSONArray vacanciesArray = (JSONArray) firstPageJson.get("items");
		Long pagesCount = (Long) firstPageJson.get("pages");

		for (int i = 1; i <= pagesCount; i++) {
			final Content getPaginationPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=" + cityId + "&search_field=name&per_page=100&page=" + i)
					.execute().returnContent();
			Object paginationPage = new JSONParser().parse(getPaginationPage.asString());
			JSONObject paginationPageJson = (JSONObject) paginationPage;
			JSONArray vacanciesPaginationArray = (JSONArray) paginationPageJson.get("items");
			vacanciesArray.addAll(vacanciesPaginationArray);

		}

		List<String> vacantiesIds = new ArrayList<>();
		for (Object vacancy : vacanciesArray) {
			JSONObject vacancyJson = (JSONObject) vacancy;
			vacantiesIds.add(vacancyJson.get("id").toString());
		}

		List<String> listSkills = new ArrayList<>();
		Set<String> unicSkills = new HashSet<>();
		long startTime = System.currentTimeMillis();

		VacancyDTO vacancyDTO = getVacanciesData(vacantiesIds);
		vacancyDataRepository.saveAll(vacancyDTO.getVacancyDataList());

		long endTime = System.currentTimeMillis();

		System.out.println( (endTime-startTime) / 1000 + " seconds");

		for (VacancyData vacancyData : vacancyDTO.getVacancyDataList()){
			listSkills.addAll(vacancyData.getSkills());
			unicSkills.addAll(vacancyData.getSkills());
		}

		Map<String, Long> countinglistSkills =new HashMap<>();
		for(String skill : unicSkills){
			countinglistSkills.put(skill, 0L);
		}

		for(String skill : listSkills){
			Long countSite = countinglistSkills.get(skill);
			if (countSite == null) {
				countinglistSkills.put(skill, 0L);
			} else {
				countSite++;
				countinglistSkills.put(skill, countSite);
			}
		}
		System.out.println(cityName);
		VacancyAnalytic vacancyAnalytic = VacancyAnalytic.builder()
				.vacancyCount((long) vacancyDTO.getNamesList().size())
				.levels(getLevelList(vacancyDTO.getNamesList(), mainSkill))
				.createAt(Instant.now().truncatedTo(ChronoUnit.DAYS))
				.skills(countinglistSkills)
				.mainSkill(mainSkill)
				.city(cityName)
				.build();
		log.debug("getVacancies: vacancyAnalytic = {}", vacancyAnalytic);
		//vacancyAnalyticRepository.save(vacancyAnalytic);
		return vacancyAnalytic;

	}



	public List<String> getVacanciesIds(String mainSkill, String cityName)  {
		//List<String> vacantiesIds = new ArrayList<>();
		String cityId = getCityId(cityName);
		List<String> vacantiesIds = new ArrayList<>();
		try {


		final Content getPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=" + cityId + "&search_field=name&per_page=100")
				.execute().returnContent();

		Object firstPage = new JSONParser().parse(getPage.asString());
		JSONObject firstPageJson = (JSONObject) firstPage;
		JSONArray vacanciesArray = (JSONArray) firstPageJson.get("items");
		Long pagesCount = (Long) firstPageJson.get("pages");

		for (int i = 1; i <= pagesCount; i++) {
			final Content getPaginationPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=" + cityId + "&search_field=name&per_page=100&page=" + i)
					.execute().returnContent();
			Object paginationPage = new JSONParser().parse(getPaginationPage.asString());
			JSONObject paginationPageJson = (JSONObject) paginationPage;
			JSONArray vacanciesPaginationArray = (JSONArray) paginationPageJson.get("items");
			vacanciesArray.addAll(vacanciesPaginationArray);

		}


		for (Object vacancy : vacanciesArray) {
			JSONObject vacancyJson = (JSONObject) vacancy;
			vacantiesIds.add(vacancyJson.get("id").toString());
		}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return vacantiesIds;

	}
	public VacancyDTO getVacancyData(String mainSkill, String cityName) {
		List<String> vacantiesIds = getVacanciesIds(mainSkill,cityName);
		return getVacanciesData(vacantiesIds);
	}


	public VacancyAnalytic getAnalytic(String mainSkill, String cityName)  {
		log.debug("getAnalytic: mainSkill = {}, cityName = {}", mainSkill, cityName);
		List<String> listSkills = new ArrayList<>();
		Set<String> unicSkills = new HashSet<>();
		long startTime = System.currentTimeMillis();
		VacancyDTO vacancyDTO = getVacancyData(mainSkill,cityName);
		long endTime = System.currentTimeMillis();

		System.out.println( (endTime-startTime) / 1000 + " seconds");

		for (VacancyData vacancyData : vacancyDTO.getVacancyDataList()){
			listSkills.addAll(vacancyData.getSkills());
			unicSkills.addAll(vacancyData.getSkills());
		}

		Map<String, Long> countinglistSkills =new HashMap<>();
		for(String skill : unicSkills){
			countinglistSkills.put(skill, 0L);
		}

		for(String skill : listSkills){
			Long countSite = countinglistSkills.get(skill);
			if (countSite == null) {
				countinglistSkills.put(skill, 0L);
			} else {
				countSite++;
				countinglistSkills.put(skill, countSite);
			}
		}

		VacancyAnalytic vacancyAnalytic = VacancyAnalytic.builder()
				.vacancyCount((long) vacancyDTO.getNamesList().size())
				.levels(getLevelList(vacancyDTO.getNamesList(), mainSkill))
				.createAt(Instant.now().truncatedTo(ChronoUnit.DAYS))
				.skills(countinglistSkills)
				.mainSkill(mainSkill)
				.city(cityName)
				.build();
		log.debug("getVacancies: vacancyAnalytic = {}", vacancyAnalytic);
		vacancyAnalyticRepository.save(vacancyAnalytic);
		return vacancyAnalytic;


	}

	public Map<String, Long> getLevelList(List<String> nameList, String mainSkill){

		Map<String, Long> levelsList = new HashMap<>();
		levelsList.put("Lead", 0L);
		levelsList.put("Senior", 0L);
		levelsList.put("Middle", 0L);
		levelsList.put("Junior", 0L);

		for (String level : nameList){

			if(level.toLowerCase().contains("lead")){
				Long count = levelsList.get("Lead");
				count++;
				levelsList.put("Lead", count);
			}
			if(level.toLowerCase().contains("senior")){
				Long count = levelsList.get("Senior");
				count++;
				levelsList.put("Senior", count);
			}
			if(level.toLowerCase().contains("middle") || level.contains(mainSkill + " Developer") || level.toLowerCase().contains(mainSkill.toLowerCase() + " engineer")){
				Long count = levelsList.get("Middle");
				count++;
				levelsList.put("Middle", count);
			}
			if(level.toLowerCase().contains("junior") || level.toLowerCase().contains("trainee")){
				Long count = levelsList.get("Junior");
				count++;
				levelsList.put("Junior", count);
			}

		}
		return levelsList;

	}

	public VacancyDTO getVacanciesData (List<String> vacanciesIds) {

		List<VacancyData> vacancyDataList = new ArrayList<>();
		List<String> namesList = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(80);
		List<Callable<Object>> tasks = new ArrayList<>();
		for (String vacancyId :vacanciesIds) {
			tasks.add(new Callable<Object>() {
				public Object call() throws Exception {

					final Content vacancy = Request.Get("https://api.hh.ru/vacancies/" + vacancyId)
							.execute().returnContent();

					Object vacancyObject = new JSONParser().parse(vacancy.asString());
					JSONObject vacancyJson = (JSONObject) vacancyObject;

					String vacancyName = (String) vacancyJson.get("name");
					namesList.add(vacancyName);
					String url = (String) vacancyJson.get("alternate_url");

					JSONObject cityObject = (JSONObject) vacancyJson.get("area");
					String city = (String) cityObject.get("name");

					JSONObject employerObject = (JSONObject) vacancyJson.get("employer");
					String employerName = (String) employerObject.get("name");

					List<String> skills= new ArrayList<>();
					JSONArray skillsArray = (JSONArray) vacancyJson.get("key_skills");

					for (Object skill : skillsArray){
						JSONObject skillObject= (JSONObject) skill;
						skills.add(skillObject.get("name").toString());
					}

					vacancyDataList.add(VacancyData.builder()
							.url(url)
							.name(vacancyName)
							.companyName(employerName)
							.skills(skills)
							.city(city)
							.createAt(Instant.now().truncatedTo(ChronoUnit.DAYS))
							.build());

					return null;
				}
			});
		}

		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();

		return VacancyDTO.builder()
				.vacancyDataList(vacancyDataList)
				.namesList(namesList)
				.build();

	}

	public String getCityId (String cityName){
		Map<String,String> namesMap = new HashMap<>();
		namesMap.put("Беларусь", "16");
		namesMap.put("Брест","1007");
		namesMap.put("Брестская область","2233");
		namesMap.put("Витебск","1005");
		namesMap.put("Витебская область","2234");
		namesMap.put("Гомель","1003");
		namesMap.put("Гомельская область","2235");
		namesMap.put("Гродненская область","2236");
		namesMap.put("Гродно","1006");
		namesMap.put("Минск","1002");
		namesMap.put("Минская область","2237");
		namesMap.put("Могилев","1004");
		namesMap.put("Могилевская область","2238");

		return namesMap.get(cityName);
	}
}

