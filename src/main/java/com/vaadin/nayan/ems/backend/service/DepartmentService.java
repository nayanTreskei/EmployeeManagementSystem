package com.vaadin.nayan.ems.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.vaadin.nayan.ems.backend.entity.*;
import com.vaadin.nayan.ems.backend.repository.*;
@Service
public class DepartmentService {
	private DepartmentRepository departmentRepository;
	
	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}
	
	
	public  List<Department> findAll() {
		return departmentRepository.findAll();
	}
	
	
}
