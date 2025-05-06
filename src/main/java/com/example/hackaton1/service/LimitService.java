package com.example.hackaton1.service;

import com.example.hackaton1.entity.Limit;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LimitService {

    private final LimitRepository limitRepository;

    @Autowired
    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    public List<Limit> getAllLimits() {
        return limitRepository.findAll();
    }

    public Limit getLimitById(Long id) {
        return limitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Limit not found with id: " + id));
    }

    public List<Limit> getLimitsByUser(User user) {
        return limitRepository.findByUser(user);
    }

    public Optional<Limit> findLimitByUserAndModelType(User user, String modelType) {
        return limitRepository.findByUserAndModelType(user, modelType);
    }

    public Limit createLimit(Limit limit) {
        Optional<Limit> existingLimit = limitRepository.findByUserAndModelType(
                limit.getUser(), limit.getModelType());

        if (existingLimit.isPresent()) {
            throw new RuntimeException("Limit for this model type already exists for this user");
        }

        return limitRepository.save(limit);
    }

    public Limit updateLimit(Long id, Limit limitDetails) {
        Limit limit = getLimitById(id);
        limit.setMaxRequests(limitDetails.getMaxRequests());
        limit.setMaxTokens(limitDetails.getMaxTokens());
        limit.setWindowMinutes(limitDetails.getWindowMinutes());
        return limitRepository.save(limit);
    }
}