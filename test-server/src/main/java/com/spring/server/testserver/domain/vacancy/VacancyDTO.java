package com.spring.server.testserver.domain.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDTO {
	private List<String> namesList;
	List<VacancyData> vacancyDataList;
}
