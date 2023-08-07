package com.example.practice.controller;

import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final ReaderService readerService;
    private final LogService logService;

    public BookController(BookService bookService, ReaderService readerService, LogService logService) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll(@AuthenticationPrincipal UserDetails userDetails) {
        var reader = readerService.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
        var books = logService.findAllBooksByReader(reader);
        return  ResponseEntity.ok(books);
    }

}
