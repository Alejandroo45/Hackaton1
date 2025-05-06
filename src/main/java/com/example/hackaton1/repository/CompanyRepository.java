package com.example.hackaton1.repository;

import com.example.hackaton1.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByRuc(String ruc);
    boolean existsByRuc(String ruc);
}