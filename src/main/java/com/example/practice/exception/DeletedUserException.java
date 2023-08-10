package com.example.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeletedUserException extends RuntimeException {

    public DeletedUserException() {
    }

    public DeletedUserException(String message) {
        super(message);
    }
}
