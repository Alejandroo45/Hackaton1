package com.example.hackaton1.service;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    public Optional<Company> findCompanyByRuc(String ruc) {
        return companyRepository.findByRuc(ruc);
    }

    public Company createCompany(Company company) {
        if (companyRepository.existsByRuc(company.getRuc())) {
            throw new RuntimeException("Company with RUC already exists");
        }
        company.setAffiliationDate(LocalDateTime.now());
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = getCompanyById(id);
        company.setName(companyDetails.getName());
        // No actualizamos el RUC porque es un identificador único
        return companyRepository.save(company);
    }

    public Company changeCompanyStatus(Long id, boolean active) {
        Company company = getCompanyById(id);
        company.setActive(active);
        return companyRepository.save(company);
    }
}