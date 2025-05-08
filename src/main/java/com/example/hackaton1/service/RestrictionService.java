package com.example.hackaton1.service;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.entity.Restriciones;
import com.example.hackaton1.repository.RestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestrictionService {

    private final RestrictionRepository restrictionRepository;

    @Autowired
    public RestrictionService(RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
    }

    public List<Restriciones> getAllRestrictions() {
        return restrictionRepository.findAll();
    }

    public Restriciones getRestrictionById(Long id) {
        return restrictionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restriction not found with id: " + id));
    }

    public List<Restriciones> getRestrictionsByCompany(Company company) {
        return restrictionRepository.findByCompany(company);
    }

    public Optional<Restriciones> findRestrictionByCompanyAndModelType(Company company, String modelType) {
        return restrictionRepository.findByCompanyAndModelType(company, modelType);
    }

    public Restriciones createRestriction(Restriciones restriction) {
        Optional<Restriciones> existingRestriction = restrictionRepository.findByCompanyAndModelType(
                restriction.getCompany(), restriction.getModelType());

        if (existingRestriction.isPresent()) {
            throw new RuntimeException("Restriction for this model type already exists for this company");
        }

        return restrictionRepository.save(restriction);
    }

    public Restriciones updateRestriction(Long id, Restriciones restrictionDetails) {
        Restriciones restriction = getRestrictionById(id);
        restriction.setMaxRequests(restrictionDetails.getMaxRequests());
        restriction.setMaxTokens(restrictionDetails.getMaxTokens());
        restriction.setWindowMinutes(restrictionDetails.getWindowMinutes());
        return restrictionRepository.save(restriction);
    }

    public void deleteRestriction(Long id) {
        Restriciones restriction = getRestrictionById(id);
        restrictionRepository.delete(restriction);
    }
}//a