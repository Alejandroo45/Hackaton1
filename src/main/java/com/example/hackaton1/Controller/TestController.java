package com.example.hackaton1.Controller;

import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import com.example.hackaton1.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final AIService aiService;

    @Autowired
    public TestController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API funcionando correctamente");
    }

    @PostMapping("/ai")
    public ResponseEntity<ChatResponseDTO> testAI(@RequestBody ChatRequestDTO chatRequest) {
        ChatResponseDTO response = aiService.processAIRequest(chatRequest);
        return ResponseEntity.ok(response);
    }
}