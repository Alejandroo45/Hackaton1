package com.example.hackaton1.Controller;

import com.example.hackaton1.entity.Restriciones;
import com.example.hackaton1.service.RestrictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
public class RestrictionController {

    private final RestrictionService restrictionService;

    @Autowired
    public RestrictionController(RestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }

    @GetMapping
    public ResponseEntity<List<Restriciones>> getAllRestrictions() {
        // En un sistema real, esto debería filtrar restricciones por la empresa del admin logueado
        return ResponseEntity.ok(restrictionService.getAllRestrictions());
    }

    @PostMapping
    public ResponseEntity<Restriciones> createRestriction(@RequestBody Restriciones restriction) {
        // En un sistema real, deberías asignar automáticamente la empresa del admin logueado
        return new ResponseEntity<>(restrictionService.createRestriction(restriction), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restriciones> updateRestriction(@PathVariable Long id, @RequestBody Restriciones restriction) {
        return ResponseEntity.ok(restrictionService.updateRestriction(id, restriction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestriction(@PathVariable Long id) {
        restrictionService.deleteRestriction(id);
        return ResponseEntity.noContent().build();
    }
}