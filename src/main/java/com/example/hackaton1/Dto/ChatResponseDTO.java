package com.example.hackaton1.Dto;

import lombok.Data;

@Data
public class ChatResponseDTO {
    private String response;
    private Integer tokensConsumed;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(String response, Integer tokensConsumed) {
        this.response = response;
        this.tokensConsumed = tokensConsumed;
    }
}