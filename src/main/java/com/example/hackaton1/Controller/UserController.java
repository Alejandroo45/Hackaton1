package com.example.hackaton1.Controller;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.entity.Limit;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.service.CompanyService;
import com.example.hackaton1.service.LimitService;
import com.example.hackaton1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;
    private final LimitService limitService;

    @Autowired
    public UserController(UserService userService, CompanyService companyService, LimitService limitService) {
        this.userService = userService;
        this.companyService = companyService;
        this.limitService = limitService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // En un sistema real, esto debería filtrar usuarios por la empresa del admin logueado
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // En un sistema real, deberías asignar automáticamente la empresa del admin logueado
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PostMapping("/{id}/limits")
    public ResponseEntity<Limit> assignLimitToUser(@PathVariable Long id, @RequestBody Limit limit) {
        User user = userService.getUserById(id);
        limit.setUser(user);
        return new ResponseEntity<>(limitService.createLimit(limit), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<String> getUserConsumption(@PathVariable Long id) {

        return ResponseEntity.ok("Reporte de consumo para el usuario ID: " + id);
    }
}