package com.spring.server.testserver.domain.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Builder

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VacancyDataDTO {

	private String name;
	private String companyName;
	private String city;
	private String url;
	private Instant createAt;
}
