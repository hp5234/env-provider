package com.craftconnect.envprovider.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craftconnect.envprovider.dto.FileInfoDTO;
import com.craftconnect.envprovider.service.FileProviderService;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileProviderService fileProviderService;

    // 1. 파일 정보(JSON DTO) 응답 컨트롤러
    @GetMapping("/info/{appName}")
    public List<FileInfoDTO> getFileInfo(@PathVariable String appName) {
        return fileProviderService.getLatestFileInfo(appName);
    }

    // 2. 파일 데이터 응답 컨트롤러
    @GetMapping("/download/{appName}/{latestDir}/{filename}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String appName,
            @PathVariable String latestDir,
            @PathVariable String filename) throws MalformedURLException {
        Resource resource = fileProviderService.getFileResource(appName, latestDir, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
} 