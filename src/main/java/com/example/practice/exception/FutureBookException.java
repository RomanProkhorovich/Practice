package com.example.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class FutureBookException extends RuntimeException {
    public FutureBookException() {
    }

    public FutureBookException(String message) {
        super(message);
    }
}
