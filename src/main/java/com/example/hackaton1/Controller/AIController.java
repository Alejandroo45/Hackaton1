package com.example.hackaton1.Controller;

import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.service.AIService;
import com.example.hackaton1.service.RequestService;
import com.example.hackaton1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final RequestService requestService;
    private final UserService userService;
    private final AIService aiService;

    @Autowired
    public AIController(RequestService requestService, UserService userService, AIService aiService) {
        this.requestService = requestService;
        this.userService = userService;
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chatWithAI(@RequestBody ChatRequestDTO chatRequest) {
        // Obtener el usuario actual
        User user = getCurrentUser();

        // Procesar la solicitud de IA
        ChatResponseDTO responseDTO = aiService.processAIRequest(chatRequest);

        // Guardar la solicitud
        Request request = new Request();
        request.setUser(user);
        request.setModelType(chatRequest.getModelType());
        request.setQuery(chatRequest.getPrompt());
        request.setResponse(responseDTO.getResponse());
        request.setTokensConsumed(responseDTO.getTokensConsumed());
        requestService.createRequest(request);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getAvailableModels() {
        return ResponseEntity.ok(List.of(
                "gpt-3.5-turbo",
                "gpt-4",
                "claude"
        ));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}