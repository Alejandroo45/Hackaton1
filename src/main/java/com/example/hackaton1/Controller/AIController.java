package com.example.hackaton1.Controller;

import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import com.example.hackaton1.Dto.MultimodalRequestDTO;
import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.service.AIService;
import com.example.hackaton1.service.RequestService;
import com.example.hackaton1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Verificar límites
        if (!aiService.checkLimits(user, chatRequest.getModelType())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ChatResponseDTO("Has excedido tu límite de consultas. Inténtalo más tarde.", 0));
        }

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

    @PostMapping("/multimodal")
    public ResponseEntity<ChatResponseDTO> multimodalRequest(@RequestBody MultimodalRequestDTO request) {
        // Obtener el usuario actual
        User user = getCurrentUser();

        // Verificar límites
        if (!aiService.checkLimits(user, request.getModelType())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ChatResponseDTO("Has excedido tu límite de consultas. Inténtalo más tarde.", 0));
        }

        // Procesar la solicitud de IA multimodal
        ChatResponseDTO responseDTO = aiService.processMultimodalRequest(request);

        // Guardar la solicitud
        Request savedRequest = new Request();
        savedRequest.setUser(user);
        savedRequest.setModelType(request.getModelType());
        savedRequest.setQuery(request.getPrompt());
        savedRequest.setResponse(responseDTO.getResponse());
        savedRequest.setTokensConsumed(responseDTO.getTokensConsumed());
        savedRequest.setFileName("imagen_" + System.currentTimeMillis() + ".jpg");  // Nombre simulado
        requestService.createRequest(savedRequest);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getAvailableModels() {
        return ResponseEntity.ok(List.of(
                "gpt-3.5-turbo",
                "gpt-4",
                "claude",
                "llama",
                "multimodal-gpt4"
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Request>> getRequestHistory() {
        User user = getCurrentUser();
        List<Request> history = requestService.getRequestsByUser(user);
        return ResponseEntity.ok(history);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}