package com.vaadin.nayan.ems.backend.service;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.vaadin.nayan.ems.backend.entity.*;
import com.vaadin.nayan.ems.backend.repository.*;


@Service
public class EmployeeService {

	//members
		private static final Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());
		private EmployeeRepository employeeRepository;
		private DepartmentRepository departmentRepository;
		private CompanyRepository companyRepository;
		
		//Constructor
		public EmployeeService(EmployeeRepository employeeRepository,
				DepartmentRepository departmentRepository, CompanyRepository companyRepository)
		{
			this.employeeRepository = employeeRepository;
			this.departmentRepository = departmentRepository;	
			this.companyRepository = companyRepository;
		}
		

		public List<Employee> findAll() {
			return employeeRepository.findAll();
		}
		
		public List<Employee> findAll(String stringFilter)
		{
			if (stringFilter == null || stringFilter.isEmpty())
			{
				return employeeRepository.findAll();
			}
			else {
				return employeeRepository.search(stringFilter);
			}
			
		}
		
		public long count() {
			return employeeRepository.count();
		}
		
		
		public void delete(Employee emp) {
			employeeRepository.delete(emp);
		}
		
		public void save(Employee emp) {
			//make sure employee is not null, return log message to console.
			if(emp == null) {
				LOGGER.log(Level.SEVERE,
						"Employee is null. is your form connected to the application?");
				return;
			}
			employeeRepository.save(emp);
		}
		

		@PostConstruct
		public void populateTestData() {
			if(departmentRepository.count() == 0) {
				departmentRepository.saveAll(
						Stream.of("HR","Marketing","Finance","Dev","IT","Cybersecurity","Executive","R&D")
						.map(Department::new)
						.collect(Collectors.toList()));
			}
			
			if(companyRepository.count() == 0) {
				companyRepository.saveAll(
						Stream.of("Metquay","Treskei","MNC","Accenture","Tech Mahindra","Cognizant","TCS","Concentrix")
						.map(Company::new)
						.collect(Collectors.toList()));
			}
			
			
			
			if(employeeRepository.count()==0) {
				Random r = new Random(0);
				List<Department> departments = departmentRepository.findAll();
				List<Company> companys = companyRepository.findAll();
				employeeRepository.saveAll(
						Stream.of("Rich Bowers", "Mark Rubio","Andrew Stich", "Andy Roo",
								"Steven McDonald","Christopher Jabbour",
								"Lucifer Morningstar",
								"Daniel Birmingham", "Matthew Knudsvig", 
								"Luke Bell", "Stephen Jabbour", "Victor Jabbour",
								"Makar Tchekalenkov", "Daniel Lopez", "Raymond Moorhead", 
								"Daniel Larrea","Jabid Methun", "Joshua Anderson","Elizabeth Grauvogel",
								"Charles Dunn", "Keith Prince")
						.map(name -> {
							String[] split = name.split(" ");
							Employee emp= new Employee();
							emp.setFirstName(split[0]);
							emp.setLastName(split[1]);
							emp.setDepartment(departments.get(r.nextInt(departments.size())));
							emp.setCompany(companys.get(r.nextInt(companys.size())));
							emp.setStatus(Employee.Status.values()[r.nextInt(Employee.Status.values().length)]);
							String email = (emp.getFirstName() + "." + emp.getLastName()+ "@" + emp.getDepartment().getName().replaceAll("[\\s-]", "") + ".com").toLowerCase(); 
							emp.setEmail(email);
							return emp;
						}).collect(Collectors.toList()));
				}
		}
		
}
