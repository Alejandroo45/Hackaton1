package com.example.hackaton1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restrictions")
public class Restriciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String modelType;

    @Column(nullable = false)
    private int maxRequests;

    @Column(nullable = false)
    private int maxTokens;

    @Column(nullable = false)
    private int windowMinutes;
}