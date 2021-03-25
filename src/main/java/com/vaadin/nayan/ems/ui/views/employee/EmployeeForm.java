package com.vaadin.nayan.ems.ui.views.employee;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.nayan.ems.backend.entity.*;

public class EmployeeForm extends FormLayout {

	private Employee employee;

	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	EmailField email = new EmailField("Email");
	//TextField company = new TextField("Company");
	ComboBox<Employee.Status> status = new ComboBox<>("Status");
	ComboBox<Department> department = new ComboBox<>("Department");
	ComboBox<Company> company = new ComboBox<>("Company");

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");

	Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);
	
	 //notification
    Notification notification = new Notification();

	// Constructor
	public EmployeeForm(List<Department> departments, List<Company> companys ) { //modified
		addClassName("employee-form");
		binder.bindInstanceFields(this);
		department.setItems(departments);
		department.setItemLabelGenerator(Department::getName);
		company.setItems(companys);
		company.setItemLabelGenerator(Company::getName);
		status.setItems(Employee.Status.values());
		add(firstName, lastName, email, department, status,company, createButtonsLayout());
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
		binder.readBean(employee);
	}

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);
		
		save.addClickListener(event -> validateAndSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		
		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		return new HorizontalLayout(save, delete, close);
	}
	
	private void validateAndSave() {
		try {
			binder.writeBean(employee);
			fireEvent(new SaveEvent(this, employee));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		
		 Notification.show(
	              "Employee Saved Sucessfully");
	}

	// Events
	
	public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
		private Employee employee;

		protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
			super(source, false);
			this.employee = employee;
		}

		public Employee getEmployee() {
			return employee;
		}
	}

	public static class SaveEvent extends EmployeeFormEvent {
		SaveEvent(EmployeeForm source, Employee employee) {
			super(source, employee);
		}
	}

	public static class DeleteEvent extends EmployeeFormEvent {
		DeleteEvent(EmployeeForm source, Employee employee) {
			super(source, employee);

			 Notification.show(
		              "Employee Deleated Sucessfully");
		}
	}

	public static class CloseEvent extends EmployeeFormEvent {
		CloseEvent(EmployeeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}