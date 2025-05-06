package com.example.hackaton1.repository;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.entity.Restriciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriciones, Long> {
    List<Restriciones> findByCompany(Company company);
    Optional<Restriciones> findByCompanyAndModelType(Company company, String modelType);
}