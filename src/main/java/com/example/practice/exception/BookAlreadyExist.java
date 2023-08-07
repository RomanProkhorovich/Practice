package com.example.practice.exception;

public class BookAlreadyExist extends RuntimeException {
    public BookAlreadyExist(String message) {
        super(message);
    }
}
