package com.craftconnect.envprovider.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MalformedURLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMalformedURLException(MalformedURLException ex) {
        return "잘못된 파일 경로입니다.";
    }
} 