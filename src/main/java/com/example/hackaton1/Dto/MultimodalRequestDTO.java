package com.example.hackaton1.Dto;

import lombok.Data;

@Data
public class MultimodalRequestDTO {
    private String prompt;
    private String modelType;
    private String imageData;
}