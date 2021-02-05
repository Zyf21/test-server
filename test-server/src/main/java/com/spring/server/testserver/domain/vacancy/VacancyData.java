package com.spring.server.testserver.domain.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Builder
@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String companyName;
	private String city;
	@ElementCollection(targetClass=String.class)
	private List<String> skills;
	private String url;
	private Instant createAt;


}
