package com.example.practice.exception;

public class DeletedUserException extends RuntimeException {

    public DeletedUserException() {
    }

    public DeletedUserException(String message) {
        super(message);
    }
}
