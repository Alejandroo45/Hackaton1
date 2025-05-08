package com.example.hackaton1.service;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.example.hackaton1.Dto.ChatRequestDTO;
import com.example.hackaton1.Dto.ChatResponseDTO;
import com.example.hackaton1.Dto.MultimodalRequestDTO;
import com.example.hackaton1.entity.Limit;
import com.example.hackaton1.entity.Request;
import com.example.hackaton1.entity.Restriciones;
import com.example.hackaton1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ChatCompletionsClient chatClient;

    public AIService() {
        // Usar directamente el token (solo para pruebas)
        String key = "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";

        System.out.println("Usando token configurado manualmente");

        String endpoint = "https://models.github.ai/inference";

        try {
            System.out.println("Inicializando cliente con token manual y endpoint: " + endpoint);
            this.chatClient = new ChatCompletionsClientBuilder()
                    .credential(new AzureKeyCredential(key))
                    .endpoint(endpoint)
                    .buildClient();
            System.out.println("✅ Cliente inicializado correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar cliente: " + e.getMessage());
            e.printStackTrace();
            this.chatClient = null;
        }
    }

    public ChatResponseDTO processAIRequest(ChatRequestDTO chatRequest) {
        if (chatClient != null) {
            try {
                System.out.println("📤 Intentando usar integración real...");
                return processRealAIRequest(chatRequest);
            } catch (Exception e) {
                System.err.println("❌ Error con la integración real: " + e.getMessage());
                e.printStackTrace();
                System.out.println("⚠️ Volviendo a simulación como respaldo");
                return processSimulatedAIRequest(chatRequest);
            }
        } else {
            System.out.println("🔸 Usando simulación (cliente no disponible)");
            return processSimulatedAIRequest(chatRequest);
        }
    }

    private ChatResponseDTO processRealAIRequest(ChatRequestDTO chatRequest) {
        // Usar el modelo de Microsoft en lugar del de Meta
        String model = "microsoft/Phi-4-Reasoning";

        System.out.println("🔄 Enviando solicitud al modelo: " + model);
        System.out.println("📝 Prompt: " + chatRequest.getPrompt());

        try {
            List<ChatRequestMessage> messages = new ArrayList<>();
            messages.add(new ChatRequestSystemMessage("You are a helpful assistant. Reply in Spanish."));
            messages.add(new ChatRequestUserMessage(chatRequest.getPrompt()));

            ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
            options.setModel(model);

            System.out.println("⏳ Enviando solicitud al API...");
            ChatCompletions completions = chatClient.complete(options);
            System.out.println("✅ Respuesta recibida del API");

            if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
                String response = completions.getChoices().get(0).getMessage().getContent();
                System.out.println("📥 Contenido de respuesta: " + response.substring(0, Math.min(50, response.length())) + "...");

                int tokens;
                if (completions.getUsage() != null) {
                    tokens = completions.getUsage().getTotalTokens();
                    System.out.println("🔢 Tokens usados (reportados): " + tokens);
                } else {
                    tokens = (chatRequest.getPrompt().length() + response.length()) / 4;
                    System.out.println("🔢 Tokens usados (estimados): " + tokens);
                }

                return new ChatResponseDTO(response, tokens);
            } else {
                System.out.println("⚠️ No se recibieron opciones en la respuesta");
                return new ChatResponseDTO("No se recibieron respuestas del modelo.", 0);
            }
        } catch (Exception e) {
            System.err.println("❌ Error procesando solicitud: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private ChatResponseDTO processSimulatedAIRequest(ChatRequestDTO chatRequest) {
        String modelType = chatRequest.getModelType();

        if (modelType.contains("gpt")) {
            return processOpenAIRequest(chatRequest);
        } else if (modelType.contains("claude")) {
            return processAnthropicRequest(chatRequest);
        } else if (modelType.contains("llama")) {
            return processMetaRequest(chatRequest);
        } else if (modelType.contains("deepspeak")) {
            return processDeepSpeakRequest(chatRequest);
        } else {
            return fallbackResponse(chatRequest);
        }
    }

    private ChatResponseDTO processOpenAIRequest(ChatRequestDTO chatRequest) {
        try {
            String prompt = chatRequest.getPrompt();
            String modelType = chatRequest.getModelType();

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
        return (prompt.length() + response.length()) / 4;
    }

    private ChatResponseDTO fallbackResponse(ChatRequestDTO chatRequest) {
        String prompt = chatRequest.getPrompt();
        String response = "Respuesta generada por modelo genérico: " +
                generateSimulatedResponse(prompt, "Sparky AI");
        int tokenCount = calculateEstimatedTokens(prompt, response);

        return new ChatResponseDTO(response, tokenCount);
    }

    public ChatResponseDTO processMultimodalRequest(MultimodalRequestDTO request) {
        try {
            String prompt = request.getPrompt();
            String modelType = request.getModelType();

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
        Optional<Limit> userLimit = limitService.findLimitByUserAndModelType(user, modelType);
        if (userLimit.isPresent()) {
            Limit limit = userLimit.get();

            List<Request> requests = requestService.getRequestsByUserAndModelInTimeWindow(
                    user, modelType, limit.getWindowMinutes());
            if (requests.size() >= limit.getMaxRequests()) {
                return false;
            }

            Integer tokenCount = requestService.getTotalTokensConsumedByUserAndModelInTimeWindow(
                    user, modelType, limit.getWindowMinutes());
            if (tokenCount != null && tokenCount >= limit.getMaxTokens()) {
                return false;
            }
        }

        if (user.getCompany() != null) {
            Optional<Restriciones> companyRestriction =
                    restrictionService.findRestrictionByCompanyAndModelType(
                            user.getCompany(), modelType);
            if (companyRestriction.isPresent()) {
            }
        }

        return true;
    }
}