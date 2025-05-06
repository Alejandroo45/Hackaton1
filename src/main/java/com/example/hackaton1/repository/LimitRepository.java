package com.example.hackaton1.repository;

import com.example.hackaton1.entity.Limit;
import com.example.hackaton1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    List<Limit> findByUser(User user);
    Optional<Limit> findByUserAndModelType(User user, String modelType);
}