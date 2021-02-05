package com.spring.server.testserver.domain.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Builder
@Entity
@Table(name = "vacancy_analytic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyAnalytic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long vacancyCount;
	private Instant createAt;
	@ElementCollection
	private Map<String, Long> skills;
	@ElementCollection
	private Map<String, Long> levels;

}
