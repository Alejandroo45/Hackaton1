package com.example.hackaton1.Dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String prompt;
    private String modelType;
}