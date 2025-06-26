package com.craftconnect.envprovider.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfoDTO {
    /**
     * 예시: "2024_06_01_153000/abc.yml"
     */
    private final String fileName;
    
    /**
     * 예시: "src/main/resources"
     */
    private final String targetFileLocation;
} 