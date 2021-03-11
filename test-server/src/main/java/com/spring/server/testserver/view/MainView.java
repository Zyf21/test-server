package com.spring.server.testserver.view;

import com.spring.server.testserver.domain.vacancy.VacancyData;
import com.spring.server.testserver.domain.vacancy.VacancyDataDTO;
import com.spring.server.testserver.repositories.VacancyDataRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import sun.tools.jar.resources.jar;

import java.util.ArrayList;
import java.util.List;

@Route
public class MainView extends VerticalLayout {

	private final VacancyDataRepository vacancyDataRepository;
	private Grid<VacancyDataDTO> grid = new Grid<>(VacancyDataDTO.class);
	private final TextField filter = new TextField();
	private final HorizontalLayout toolbar = new HorizontalLayout(filter);

	@Autowired
	public MainView(VacancyDataRepository vacancyDataRepository) {
		this.vacancyDataRepository = vacancyDataRepository;

		filter.setPlaceholder("Type to filter");
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(field -> showCompany(field.getValue()));
		add( toolbar, grid);
		grid.setHeightByRows(true);
		grid.setHeight("400px");
		grid.getColumns().forEach(col->col.setWidth("50px"));



		showCompany("");

	}



	private void showCompany(String companyName) {

		//grid.setSizeFull();
		if (companyName.isEmpty()) {

			List<VacancyData> vacancyDataList = vacancyDataRepository.findAll();
			List<VacancyDataDTO> vacancyDataDTOList = new ArrayList<>();
			for (VacancyData vacancyData : vacancyDataList) {
				vacancyDataDTOList.add(VacancyDataDTO.builder()
						.name(vacancyData.getName())
						.companyName(vacancyData.getCompanyName())
						.city(vacancyData.getCity())
						.url(vacancyData.getUrl())
						.createAt(vacancyData.getCreateAt())
						.build());
				grid.setItems(vacancyDataDTOList);

			}
		} else {

			List<VacancyData> vacancyDataList = vacancyDataRepository.findByCompanyName(companyName);
			List<VacancyDataDTO> vacancyDataDTOList = new ArrayList<>();
			for (VacancyData vacancyData : vacancyDataList) {
				vacancyDataDTOList.add(VacancyDataDTO.builder()
						.name(vacancyData.getName())
						.companyName(vacancyData.getCompanyName())
						.city(vacancyData.getCity())
						.url(vacancyData.getUrl())
						.createAt(vacancyData.getCreateAt())
						.build());
				grid.setItems(vacancyDataDTOList);

			}
		}


	}
}



