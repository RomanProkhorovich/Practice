package com.example.practice.exception;

import java.time.LocalDateTime;

public class AppError {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;

    public AppError(LocalDateTime timestamp, int status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
