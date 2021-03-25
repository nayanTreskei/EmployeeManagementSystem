package com.vaadin.nayan.ems.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.nayan.ems.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
