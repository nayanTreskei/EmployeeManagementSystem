package com.vaadin.nayan.ems.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaadin.nayan.ems.backend.entity.*;

public interface EmployeeRepository extends JpaRepository<com.vaadin.nayan.ems.backend.entity.Employee, Long> {

	@Query("select e from Employee e "
			+ "where lower(e.firstName) like lower(concat('%', :searchTerm, '%'))"
			+ "or lower(e.lastName) like lower(concat('%', :searchTerm, '%'))")
	List<Employee> search(@Param("searchTerm")String stringTerm);

}