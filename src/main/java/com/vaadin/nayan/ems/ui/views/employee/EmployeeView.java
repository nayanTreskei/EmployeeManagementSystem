package com.vaadin.nayan.ems.ui.views.employee;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.nayan.ems.backend.entity.*;
import com.vaadin.nayan.ems.backend.repository.*;
import com.vaadin.nayan.ems.backend.service.*;
import com.vaadin.nayan.ems.ui.MainLayout;

import java.util.List;
import java.util.Map;

@PageTitle("Employee | EMS")
@Route(value = "employee", layout = MainLayout.class)
public class EmployeeView extends VerticalLayout {
	
	public EmployeeService employeeService;
	private Grid<Employee> grid = new Grid<>(Employee.class);
	Grid<Employee> employeeGrid = new Grid<>(Employee.class);
	private TextField filterText = new TextField();
	public EmployeeForm empForm;
	
	public EmployeeView (EmployeeService employeeService, DepartmentService departmentService, CompanyService companyService) { //modified
		this.employeeService = employeeService;
		addClassName("list-view"); 
		setSizeFull(); 

		configureGrid();
		getToolbar();
		
		empForm = new EmployeeForm(departmentService.findAll(), companyService.findAll());
		empForm.addListener(EmployeeForm.SaveEvent.class, this::saveEmp);
		empForm.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmp);
		empForm.addListener(EmployeeForm.CloseEvent.class, e -> closeEditor());
		

		//DivContent
		Div content = new Div( grid, empForm); //compForm m3
		content.addClassName("content"); 
		content.setSizeFull();
		add(getToolbar(), content);  //compForm m3
		updateList();
		closeEditor();
		
	}
	
	private void saveEmp(EmployeeForm.SaveEvent event) {
		employeeService.save(event.getEmployee());
		updateList();
		closeEditor();
	}

	
	private void deleteEmp(EmployeeForm.DeleteEvent event) {
		employeeService.delete(event.getEmployee());
		updateList();
		closeEditor();
	}

	
	private void closeEditor() {
		empForm.setEmployee(null);
		empForm.setVisible(false);
		removeClassName("editing");
	}

	
	public void editEmployee(Employee employee) {
		if (employee == null) {
			closeEditor();
		} else {
			empForm.setEmployee(employee);
			empForm.setVisible(true);
			addClassName("editing");
		}
	}

	
	private void configureGrid() {
		grid.addClassName("employee-grid");
		grid.setSizeFull();
		grid.removeColumnByKey("department");
		grid.setColumns("firstName", "lastName", "email", "status");
		grid.addColumn(emp -> {
			Department department = emp.getDepartment();
			return department == null ? "-" : department.getName();
		}).setHeader("Department");
		grid.addColumn(emp -> {
			Company company = emp.getCompany();
			return company == null ? "-" : company.getName();
		}).setHeader("Company");

		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));

	}

	
	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());
		Button addEmployeeButton = new Button("Add Employee");
		addEmployeeButton.addClickListener(click -> addEmployee());
		HorizontalLayout toolbar = new HorizontalLayout(filterText, addEmployeeButton);
		toolbar.addClassName("toolbar");
		return toolbar;
		

		// TODO add a button/grid to add a department
	}
	
	
	
	
	void addEmployee() {
		grid.asSingleSelect().clear();
		editEmployee(new Employee());
	}

	
	private void updateList() {
		grid.setItems(employeeService.findAll(filterText.getValue()));
	}
	

		

	

	
		
	
	
	
	
//    private final ContactService contactService;
//    private final CompanyService companyService;
//
//    public EmployeeView(ContactService contactService,
//                         CompanyService companyService) {
//        this.contactService = contactService;
//        this.companyService = companyService;
//
//        addClassName("dashboard-view");
//        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//
//        add(
//            getContactStats(),
//            getCompaniesChart()
//        );
//    }
//
//    private Span getContactStats() {
//        Span stats = new Span(contactService.count() + " contacts");
//        stats.addClassName("contact-stats");
//
//        return stats;
//    }
//
//    private Component getCompaniesChart() {
//        Chart chart = new Chart(ChartType.PIE);
//
//        DataSeries dataSeries = new DataSeries();
//        Map<String, Integer> stats = companyService.getStats();
//        stats.forEach((name, number) ->
//            dataSeries.add(new DataSeriesItem(name, number)));
//
//        chart.getConfiguration().setSeries(dataSeries);
//        return chart;
//    }
}
