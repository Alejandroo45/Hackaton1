package com.example.hackaton1.service;

import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import com.example.hackaton1.Dto.MultimodalRequestDTO;
import com.example.hackaton1.entity.Limit;
import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.Restriciones;
import com.example.hackaton1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    @Autowired
    private LimitService limitService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RestrictionService restrictionService;

    // Simulación de integración con GitHub Models
    public ChatResponseDTO processAIRequest(ChatRequestDTO chatRequest) {
        String modelType = chatRequest.getModelType();

        // Seleccionar el servicio adecuado según el tipo de modelo
        if (modelType.contains("gpt")) {
            return processOpenAIRequest(chatRequest);
        } else if (modelType.contains("claude")) {
            return processAnthropicRequest(chatRequest);
        } else if (modelType.contains("llama")) {
            return processMetaRequest(chatRequest);
        } else if (modelType.contains("deepspeak")) {
            return processDeepSpeakRequest(chatRequest);
        } else {
            // Modelo no reconocido, usar fallback
            return fallbackResponse(chatRequest);
        }
    }

    private ChatResponseDTO processOpenAIRequest(ChatRequestDTO chatRequest) {
        try {
            // Simulación de integración con OpenAI
            String prompt = chatRequest.getPrompt();
            String modelType = chatRequest.getModelType();

            // Simulamos una respuesta de OpenAI
            String responseText = "Respuesta de OpenAI (" + modelType + "): " +
                    generateSimulatedResponse(prompt, "OpenAI");
            int tokens = calculateEstimatedTokens(prompt, responseText);

            return new ChatResponseDTO(responseText, tokens);
        } catch (Exception e) {
            return new ChatResponseDTO("Error al procesar la solicitud con OpenAI: " + e.getMessage(), 0);
        }
    }

    private ChatResponseDTO processAnthropicRequest(ChatRequestDTO chatRequest) {
        try {
            // Simulación de integración con Claude API
            String prompt = chatRequest.getPrompt();

            String responseText = "Respuesta de Claude: " +
                    generateSimulatedResponse(prompt, "Anthropic");
            int tokens = calculateEstimatedTokens(prompt, responseText);

            return new ChatResponseDTO(responseText, tokens);
        } catch (Exception e) {
            return new ChatResponseDTO("Error al procesar la solicitud con Anthropic: " + e.getMessage(), 0);
        }
    }

    private ChatResponseDTO processMetaRequest(ChatRequestDTO chatRequest) {
        try {
            // Simulación de integración con Meta API (Llama)
            String prompt = chatRequest.getPrompt();

            String responseText = "Respuesta de Meta (Llama): " +
                    generateSimulatedResponse(prompt, "Meta");
            int tokens = calculateEstimatedTokens(prompt, responseText);

            return new ChatResponseDTO(responseText, tokens);
        } catch (Exception e) {
            return new ChatResponseDTO("Error al procesar la solicitud con Meta: " + e.getMessage(), 0);
        }
    }

    private ChatResponseDTO processDeepSpeakRequest(ChatRequestDTO chatRequest) {
        try {
            // Simulación de integración con DeepSpeak API
            String prompt = chatRequest.getPrompt();

            String responseText = "Respuesta de DeepSpeak: " +
                    generateSimulatedResponse(prompt, "DeepSpeak");
            int tokens = calculateEstimatedTokens(prompt, responseText);

            return new ChatResponseDTO(responseText, tokens);
        } catch (Exception e) {
            return new ChatResponseDTO("Error al procesar la solicitud con DeepSpeak: " + e.getMessage(), 0);
        }
    }

    private String generateSimulatedResponse(String prompt, String provider) {
        String lowercasePrompt = prompt.toLowerCase();

        if (lowercasePrompt.contains("inteligencia artificial") || lowercasePrompt.contains("ia")) {
            return "La inteligencia artificial es un campo de la informática que busca crear sistemas capaces de realizar tareas que normalmente requieren inteligencia humana. " +
                    "Los modelos de " + provider + " utilizan redes neuronales avanzadas para procesar lenguaje natural.";
        } else if (lowercasePrompt.contains("capital") && lowercasePrompt.contains("francia")) {
            return "La capital de Francia es París. Según los datos de " + provider + ", París es una de las ciudades más visitadas del mundo.";
        } else if (lowercasePrompt.contains("java") || lowercasePrompt.contains("spring") || lowercasePrompt.contains("programación")) {
            return "Java es un lenguaje de programación orientado a objetos ampliamente utilizado. " +
                    provider + " tiene varias herramientas y APIs para ayudar con el desarrollo en Java.";
        } else {
            return "Basado en tu consulta: \"" + prompt + "\", " + provider +
                    " ha analizado la información disponible y puede proporcionar una respuesta detallada sobre este tema.";
        }
    }

    private int calculateEstimatedTokens(String prompt, String response) {
        // Una estimación simple basada en la longitud del texto
        return (prompt.length() + response.length()) / 4;
    }

    private ChatResponseDTO fallbackResponse(ChatRequestDTO chatRequest) {
        // Implementación de respaldo para cuando no se reconoce el modelo
        String prompt = chatRequest.getPrompt();
        String response = "Respuesta generada por modelo genérico: " +
                generateSimulatedResponse(prompt, "Sparky AI");
        int tokenCount = calculateEstimatedTokens(prompt, response);

        return new ChatResponseDTO(response, tokenCount);
    }

    public ChatResponseDTO processMultimodalRequest(MultimodalRequestDTO request) {
        try {
            // Simulación de procesamiento multimodal
            String prompt = request.getPrompt();
            String modelType = request.getModelType();

            // Simulamos una respuesta multimodal
            String response = "He analizado la imagen proporcionada junto con tu consulta: \"" +
                    prompt + "\". La imagen muestra [descripción genérica simulada]. Esta respuesta está generada por " +
                    modelType + " en modo multimodal.";

            int tokens = calculateEstimatedTokens(prompt, response);

            return new ChatResponseDTO(response, tokens);
        } catch (Exception e) {
            return new ChatResponseDTO("Error al procesar la solicitud multimodal: " + e.getMessage(), 0);
        }
    }

    public boolean checkLimits(User user, String modelType) {
        // Verificar límites de usuario
        Optional<Limit> userLimit = limitService.findLimitByUserAndModelType(user, modelType);
        if (userLimit.isPresent()) {
            Limit limit = userLimit.get();

            // Verificar número de solicitudes en la ventana de tiempo
            List<Request> requests = requestService.getRequestsByUserAndModelInTimeWindow(
                    user, modelType, limit.getWindowMinutes());
            if (requests.size() >= limit.getMaxRequests()) {
                return false;
            }

            // Verificar consumo de tokens
            Integer tokenCount = requestService.getTotalTokensConsumedByUserAndModelInTimeWindow(
                    user, modelType, limit.getWindowMinutes());
            if (tokenCount != null && tokenCount >= limit.getMaxTokens()) {
                return false;
            }
        }

        // Verificar restricciones de empresa
        if (user.getCompany() != null) {
            Optional<Restriciones> companyRestriction =
                    restrictionService.findRestrictionByCompanyAndModelType(
                            user.getCompany(), modelType);

            if (companyRestriction.isPresent()) {

            }
        }

        return true; // Si no hay límites o los límites no se han superado, permitir la solicitud
    }
}