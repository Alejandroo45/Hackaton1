package com.example.hackaton1.service;

import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    public ChatResponseDTO processAIRequest(ChatRequestDTO chatRequest) {
        // Simulamos la respuesta de IA para el hackathon
        String response = "Esta es una respuesta simulada para: " + chatRequest.getPrompt();

        // Simulamos diferentes respuestas según el modelo
        if (chatRequest.getModelType().equals("gpt-4")) {
            response += " (usando GPT-4)";
        } else if (chatRequest.getModelType().equals("claude")) {
            response += " (usando Claude)";
        } else {
            response += " (usando modelo estándar)";
        }

        // Simulamos un consumo de tokens
        int tokensConsumed = chatRequest.getPrompt().length() / 4;
        if (tokensConsumed < 1) tokensConsumed = 1;

        return new ChatResponseDTO(response, tokensConsumed);
    }
}