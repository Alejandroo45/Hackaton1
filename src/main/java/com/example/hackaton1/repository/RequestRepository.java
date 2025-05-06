package com.example.hackaton1.repository;

import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUser(User user);

    @Query("SELECT r FROM Request r WHERE r.user = :user AND r.timestamp >= :startTime")
    List<Request> findRequestsByUserInTimeWindow(@Param("user") User user, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT r FROM Request r WHERE r.user = :user AND r.modelType = :modelType AND r.timestamp >= :startTime")
    List<Request> findRequestsByUserAndModelInTimeWindow(@Param("user") User user, @Param("modelType") String modelType, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT SUM(r.tokensConsumed) FROM Request r WHERE r.user = :user AND r.timestamp >= :startTime")
    Integer sumTokensConsumedByUserInTimeWindow(@Param("user") User user, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT SUM(r.tokensConsumed) FROM Request r WHERE r.user = :user AND r.modelType = :modelType AND r.timestamp >= :startTime")
    Integer sumTokensConsumedByUserAndModelInTimeWindow(@Param("user") User user, @Param("modelType") String modelType, @Param("startTime") LocalDateTime startTime);
}