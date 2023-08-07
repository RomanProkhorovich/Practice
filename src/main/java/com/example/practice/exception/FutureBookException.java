package com.example.practice.exception;

public class FutureBookException extends RuntimeException {
    public FutureBookException() {
    }

    public FutureBookException(String message) {
        super(message);
    }
}
