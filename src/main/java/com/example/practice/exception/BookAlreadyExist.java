package com.example.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST )
public class BookAlreadyExist extends RuntimeException {
    public BookAlreadyExist(String message) {
        super(message);
    }
}
