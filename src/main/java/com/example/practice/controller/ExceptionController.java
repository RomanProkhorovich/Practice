package com.example.practice.controller;

import com.example.practice.exception.AppError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppError> handle(RuntimeException e, HttpServletResponse response){
        AppError error=new AppError(LocalDateTime.now(), response.getStatus() , e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
