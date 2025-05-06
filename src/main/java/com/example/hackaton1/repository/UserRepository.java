package com.example.hackaton1.repository;

import com.example.hackaton1.entity.Company;
import com.example.hackaton1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByCompany(Company company);
    List<User> findByCompanyAndIsAdmin(Company company, boolean isAdmin);
}