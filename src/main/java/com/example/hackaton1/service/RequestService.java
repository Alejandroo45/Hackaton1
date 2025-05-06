package com.example.hackaton1.service;

import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    public List<Request> getRequestsByUser(User user) {
        return requestRepository.findByUser(user);
    }

    public Request createRequest(Request request) {
        request.setTimestamp(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public List<Request> getRequestsByUserInTimeWindow(User user, int windowMinutes) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(windowMinutes);
        return requestRepository.findRequestsByUserInTimeWindow(user, startTime);
    }

    public List<Request> getRequestsByUserAndModelInTimeWindow(User user, String modelType, int windowMinutes) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(windowMinutes);
        return requestRepository.findRequestsByUserAndModelInTimeWindow(user, modelType, startTime);
    }

    public Integer getTotalTokensConsumedByUserInTimeWindow(User user, int windowMinutes) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(windowMinutes);
        Integer totalTokens = requestRepository.sumTokensConsumedByUserInTimeWindow(user, startTime);
        return totalTokens != null ? totalTokens : 0;
    }

    public Integer getTotalTokensConsumedByUserAndModelInTimeWindow(User user, String modelType, int windowMinutes) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(windowMinutes);
        Integer totalTokens = requestRepository.sumTokensConsumedByUserAndModelInTimeWindow(user, modelType, startTime);
        return totalTokens != null ? totalTokens : 0;
    }
}