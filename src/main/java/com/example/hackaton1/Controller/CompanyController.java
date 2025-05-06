package com.example.hackaton1.Controller;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.createCompany(company), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(id, company));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Company> changeCompanyStatus(@PathVariable Long id, @RequestBody boolean active) {
        return ResponseEntity.ok(companyService.changeCompanyStatus(id, active));
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<String> getCompanyConsumption(@PathVariable Long id) {
        // Implementar lógica para obtener el consumo de la empresa
        // Este es un placeholder, deberías implementar la lógica real
        return ResponseEntity.ok("Reporte de consumo para la empresa ID: " + id);
    }
}