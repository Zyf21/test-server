package com.spring.server.testserver.services;

import com.spring.server.testserver.domain.vacancy.VacancyAnalytic;
import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.repositories.VacancyAnalyticRepository;
import com.spring.server.testserver.repositories.VacancyDataRepository;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class HHVacanciesService {

	private final VacancyAnalyticRepository vacancyAnalyticRepository;
	private final VacancyDataRepository vacancyDataRepository;

	public HHVacanciesService(final VacancyAnalyticRepository vacancyAnalyticRepository,
							  final VacancyDataRepository vacancyDataRepository) {
		this.vacancyAnalyticRepository = vacancyAnalyticRepository;
		this.vacancyDataRepository = vacancyDataRepository;
	}


	public void getVacancies() throws IOException, ParseException {
		long startTime = System.currentTimeMillis();
		List<String> vacantiesIds = new ArrayList<>();
		List<String> vacantiesNames= new ArrayList<>();

		String mainSkill = "Java";

		Map<String, List<String>> mapa = new HashMap<>();
		final Content getPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=16&search_field=name&per_page=100")
				.execute().returnContent();


		Object firstPage = new JSONParser().parse(getPage.asString());
		JSONObject firstPageJson = (JSONObject) firstPage;
		JSONArray vacanciesArray = (JSONArray) firstPageJson.get("items");
		Long pagesCount = (Long) firstPageJson.get("pages");

		for(int i =1; i <= pagesCount; i++){
			final Content getPaginationPage = Request.Get("https://api.hh.ru/vacancies?text=" + mainSkill + "&area=16&search_field=name&per_page=100&page=" + i)
					.execute().returnContent();
			Object paginationPage = new JSONParser().parse(getPaginationPage.asString());
			JSONObject paginationPageJson = (JSONObject) paginationPage;
			JSONArray vacanciesPaginationArray = (JSONArray) paginationPageJson.get("items");
			vacanciesArray.addAll(vacanciesPaginationArray);

		}


		for (Object vacancy : vacanciesArray){
			JSONObject vacancyJson = (JSONObject) vacancy;
			vacantiesIds.add(vacancyJson.get("id").toString());
		}

		int t = 1;

		List<VacancyData> vacancyDataList = new ArrayList<>();
		List<String> listSkills = new ArrayList<>();
		Set<String> unicSkills = new HashSet<>();
		List<String> namesList = new ArrayList<>();
		for (String s : vacantiesIds){

			final Content vacancy = Request.Get("https://api.hh.ru/vacancies/" + s)
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


//			System.out.println(employerName + city);

			vacantiesNames.add(vacancyName + t);

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
			vacancyDataRepository.save(VacancyData.builder()
					.url(url)
					.name(vacancyName)
					.companyName(employerName)
					.skills(skills)
					.city(city)
					.createAt(Instant.now().truncatedTo(ChronoUnit.DAYS))
					.build());


			mapa.put(vacancyName+" " + t, skills);
			t++;


		}





		for (VacancyData vacancyData : vacancyDataList){

			System.out.println(vacancyData.getSkills().size() +" " +vacancyData.getUrl());
			listSkills.addAll(vacancyData.getSkills());
			unicSkills.addAll(vacancyData.getSkills());

		}














		Map<String, Long> countinglistSkills =new HashMap<>();
		for(String skill :unicSkills){
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

//		Map<String, Long> sortedlistSkills = counting.entrySet()
//				.stream()
//				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//				.collect(Collectors.toMap(
//						Map.Entry::getKey,
//						Map.Entry::getValue,
//						(oldValue, newValue) -> oldValue, LinkedHashMap::new));



		for (Map.Entry<String, Long> skill :countinglistSkills.entrySet()){
			System.out.println( " "+ skill.getKey() + "  " + skill.getValue());

		}





		int i = 1;
		for (Map.Entry<String, List<String>> name :mapa.entrySet()){
			System.out.println(i + " "+ name.getKey() + "  " + name.getValue());
			i++;
		}
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime)/1000 +" секунд  Всего вакансий:" +vacantiesIds.size() );
		System.out.println(vacancyDataList.size());
		int p = 1;
//		for (String s : listSkills){
//			System.out.println(s + p);
//			p++;
//		}


//		Map<String, Long> levelsList = new HashMap<>();
//		levelsList.put("Lead Java Developer", 0L);
//		levelsList.put("Senior Java Developer", 0L);
//		levelsList.put("Middle Java Developer", 0L);
//		levelsList.put("Junior Java Developer", 0L);


		Map<String, Long> levelsList = new HashMap<>();
		levelsList.put("Lead Developer", 0L);
		levelsList.put("Senior Developer", 0L);
		levelsList.put("Middle Developer", 0L);
		levelsList.put("Junior Developer", 0L);



		for (String level :namesList){

			if(level.toLowerCase().contains("lead")){
				Long count = levelsList.get("Lead Developer");
				count++;
				levelsList.put("Lead Developer", count);
			}
			if(level.toLowerCase().contains("senior")){
				Long count = levelsList.get("Senior Developer");
				count++;
				levelsList.put("Senior Developer", count);
			}
			if(level.toLowerCase().contains("middle") || level.contains(mainSkill + " Developer") || level.toLowerCase().contains(mainSkill.toLowerCase() + " engineer")){
				Long count = levelsList.get("Middle Developer");
				count++;
				levelsList.put("Middle Developer", count);
			}
			if(level.toLowerCase().contains("junior") || level.toLowerCase().contains("trainee")){
				Long count = levelsList.get("Junior Developer");
				count++;
				levelsList.put("Junior Developer", count);
			}

		}


		for (Map.Entry<String, Long> name :levelsList.entrySet()){
			System.out.println( " "+ name.getKey() + "  " + name.getValue());

		}
		System.out.println(namesList.size());
		VacancyAnalytic vacancyAnalytic = VacancyAnalytic.builder()
				.vacancyCount((long) namesList.size())
				.levels(levelsList)
				.createAt(Instant.now().truncatedTo(ChronoUnit.DAYS))
				.skills(countinglistSkills)
				.build();

		System.out.println(vacancyAnalytic);
		vacancyAnalyticRepository.save(vacancyAnalytic);

	}

	}

