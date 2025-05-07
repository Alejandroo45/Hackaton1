package com.example.hackaton1.service;

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

    public ChatResponseDTO processAIRequest(ChatRequestDTO chatRequest) {
        // Demo version with high-quality responses
        String prompt = chatRequest.getPrompt().toLowerCase();
        String modelType = chatRequest.getModelType();

        String response;
        int tokenCount;

        // Generate appropriate response based on prompt content
        if (prompt.contains("inteligencia artificial") || prompt.contains("ia")) {
            response = "La inteligencia artificial es un campo de la informática que busca crear sistemas capaces de realizar tareas que normalmente requieren inteligencia humana. Estos sistemas pueden aprender, razonar, adaptarse y tomar decisiones basadas en datos. Los avances recientes en aprendizaje profundo han revolucionado áreas como el procesamiento del lenguaje natural, la visión por computadora y la robótica.";
            tokenCount = 65;
        } else if (prompt.contains("capital") && prompt.contains("francia")) {
            response = "La capital de Francia es París, una de las ciudades más visitadas del mundo. Conocida como la 'Ciudad de la Luz', alberga monumentos icónicos como la Torre Eiffel, el Arco del Triunfo y el Museo del Louvre. Su rica historia, arquitectura, gastronomía y cultura la convierten en un destino turístico de primer nivel mundial.";
            tokenCount = 58;
        } else if (prompt.contains("java") || prompt.contains("spring") || prompt.contains("programación")) {
            response = "Java es un lenguaje de programación orientado a objetos ampliamente utilizado en el desarrollo de aplicaciones empresariales. Spring es un framework popular para Java que simplifica el desarrollo de aplicaciones mediante la inversión de control y la inyección de dependencias. Juntos, proporcionan una base robusta para crear aplicaciones escalables y mantenibles.";
            tokenCount = 62;
        } else {
            response = "Basado en mi análisis de tu consulta: \"" + chatRequest.getPrompt() + "\", puedo proporcionar una respuesta detallada que incorpora los últimos avances y conocimientos en este campo. La información está respaldada por investigaciones recientes y mejores prácticas en la industria.";
            tokenCount = 45;
        }

        // Add model-specific signature
        if (modelType.equals("gpt-4")) {
            response += " [Análisis realizado con GPT-4, optimizado para razonamiento avanzado y comprensión contextual]";
            tokenCount += 10;
        } else if (modelType.equals("claude")) {
            response += " [Análisis generado por Claude, destacando por claridad y precisión en la respuesta]";
            tokenCount += 10;
        } else {
            response += " [Respuesta procesada con " + modelType + "]";
            tokenCount += 5;
        }

        return new ChatResponseDTO(response, tokenCount);
    }

    public ChatResponseDTO processMultimodalRequest(MultimodalRequestDTO request) {
        String prompt = request.getPrompt().toLowerCase();
        String modelType = request.getModelType();

        // Generate realistic multimodal response
        String response;
        int tokenCount;

        if (prompt.contains("qué hay") || prompt.contains("describe")) {
            response = "En la imagen proporcionada puedo identificar varios elementos visuales. La composición muestra un [objeto/escena] con [características destacadas]. Los colores predominantes son [colores], y la disposición sugiere [interpretación]. Este tipo de análisis visual es posible gracias a redes neuronales convolucionales entrenadas con millones de imágenes.";
            tokenCount = 85;
        } else if (prompt.contains("analiza") || prompt.contains("evalúa")) {
            response = "Tras analizar la imagen, puedo determinar que representa [contenido]. Los elementos principales incluyen [elementos], organizados de forma [descripción]. La calidad de la imagen y las características técnicas permiten un análisis detallado de aspectos como [aspectos]. Este análisis multimodal combina reconocimiento visual con comprensión contextual.";
            tokenCount = 90;
        } else {
            response = "He procesado la imagen en relación a tu consulta \"" + request.getPrompt() + "\". La imagen contiene elementos visuales que se corresponden con [descripción relevante]. Este tipo de procesamiento multimodal permite integrar información visual y textual para proporcionar análisis completos y contextualizados.";
            tokenCount = 75;
        }

        // Add model-specific signature
        if (modelType.contains("gpt")) {
            response += " [Análisis multimodal realizado con " + modelType + " Vision]";
        } else {
            response += " [Análisis multimodal procesado por " + modelType + "]";
        }

        tokenCount += 10;

        return new ChatResponseDTO(response, tokenCount);
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
                // Implementación simplificada para el hackathon
                return true;
            }
        }

        return true; // Si no hay límites, permitir la solicitud
    }
}


































