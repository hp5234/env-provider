package com.craftconnect.envprovider.service;

import com.craftconnect.envprovider.dto.FileInfoDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FileProviderService {

    private static final String BASE_APP_FILES_PATH = "Dev/app_files";

    /**
     * [디렉토리 구조 예시]
     * Dev/app_files/{애플리케이션이름}/{YYYY_MM_DD_HHmmSS}/info.json
     *
     * [info.json 예시]
     * {
     *   "files": [
     *     {
     *       "filename": "abc.yml",
     *       "targetFileLocation": "src/main/resources"
     *     },
     *     {
     *       "filename": "abcd.yml",
     *       "targetFileLocation": "src/main/resources"
     *     }
     *   ]
     * }
     */
    public List<FileInfoDTO> getLatestFileInfo(String appName) {
        Path appDir = Paths.get(BASE_APP_FILES_PATH).resolve(appName);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(appDir)) {
            Optional<Path> latestDir =
                    StreamSupport.stream(stream.spliterator(), false)
                            .filter(Files::isDirectory)
                            .max(Comparator.comparing(Path::getFileName));
            if (latestDir.isPresent()) {
                Path infoJson = latestDir.get().resolve("info.json");
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(infoJson.toFile());
                JsonNode filesNode = root.get("files");

                if (filesNode != null && filesNode.isArray()) {
                    List<FileInfoDTO> result = new java.util.ArrayList<>();
                    String latestDirName = latestDir.get().getFileName().toString();
                    
                    for (JsonNode fileNode : filesNode) {
                        String fileName = fileNode.get("filename").asText();
                        String targetFileLocation = fileNode.get("targetFileLocation").asText();
                        String combinedFileName = latestDirName + "/" + fileName;
                        result.add(FileInfoDTO.builder()
                                .fileName(combinedFileName)
                                .targetFileLocation(targetFileLocation)
                                .build());
                    }
                    return result;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 정보를 읽는 중 오류 발생", e);
        }
        return Collections.emptyList();
    }

    /**
     * @param appName   예시: "myApp"
     * @param latestDir 예시: "2024_06_01_153000"
     * @param filename  예시: "abc.yml"
     */
    public Resource getFileResource(String appName, String latestDir, String filename) throws MalformedURLException {
        Path filePath = Paths.get(BASE_APP_FILES_PATH)
                            .resolve(appName)
                            .resolve(latestDir)
                            .resolve(filename)
                            .normalize();
                        
        return new UrlResource(filePath.toUri());
    }
} 