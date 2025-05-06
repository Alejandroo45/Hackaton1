package com.example.hackaton1.Controller;

import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.User;
import com.example.hackaton1.service.RequestService;
import com.example.hackaton1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final RequestService requestService;
    private final UserService userService;

    @Autowired
    public AIController(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chatWithAI(@RequestBody Map<String, String> chatRequest) {
        // En un sistema real, obtendrías el usuario del contexto de seguridad
        User user = userService.getUserById(1L); // Mock user ID

        // Implementar lógica para verificar límites y realizar la consulta a la IA
        Request request = new Request();
        request.setUser(user);
        request.setModelType("chat");
        request.setQuery(chatRequest.get("prompt"));
        request.setResponse("Esta es una respuesta simulada de IA.");
        request.setTokensConsumed(10); // Mock tokens

        requestService.createRequest(request);

        return ResponseEntity.ok(Map.of(
                "response", "Esta es una respuesta simulada de IA.",
                "tokensConsumed", 10
        ));
    }

    @PostMapping("/completion")
    public ResponseEntity<Map<String, Object>> getCompletion(@RequestBody Map<String, String> completionRequest) {
        // Similar al endpoint de chat, pero para completado de texto
        return ResponseEntity.ok(Map.of(
                "completion", "Este es un completado de texto simulado.",
                "tokensConsumed", 5
        ));
    }

    @PostMapping("/multimodal")
    public ResponseEntity<Map<String, Object>> multimodalRequest(@RequestBody Map<String, Object> multimodalRequest) {
        // Endpoint para consultas multimodales (con imágenes)
        return ResponseEntity.ok(Map.of(
                "response", "Esta es una respuesta multimodal simulada.",
                "tokensConsumed", 15
        ));
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getAvailableModels() {
        // Devolver lista de modelos disponibles para el usuario
        return ResponseEntity.ok(List.of(
                "gpt-4",
                "claude-3",
                "llama-3",
                "gemini-pro"
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Request>> getUserHistory() {
        // En un sistema real, obtendrías el usuario del contexto de seguridad
        User user = userService.getUserById(1L); // Mock user ID
        return ResponseEntity.ok(requestService.getRequestsByUser(user));
    }
}
//lkdsaskdaskldqkl