package com.example.practice.controller;

import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.serviceForController.BookServiceForController;
import com.example.practice.serviceForController.LogServiceForController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookServiceForController bookService;
    private final LogServiceForController logService;

    public BookController(BookServiceForController bookService, LogServiceForController logService) {
        this.bookService = bookService;
        this.logService = logService;
    }


    @Operation(
            method = "GET",
            tags = {"BOOK", "USER", "ADMIN"},
            summary = "Find all books",
            description = "Find all books. If user is ADMIN returns all existing books. If user is USER returns all his books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "books"
                    )
            })
    @GetMapping
    public ResponseEntity<List<Book>> findAll(HttpServletRequest req) {


        return ResponseEntity.ok(logService.findAllBooksByReader(getRolesFromAuthServer(req).getUsername(),req));
    }


    @Operation(
            method = "GET",
            tags = {"BOOK", "ADMIN"},
            summary = "Find book by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "book found successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "book not found"
                    )
            })
    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id,HttpServletRequest req) {


        return ResponseEntity.ok(bookService.findById(id,req));
    }


    @Operation(
            method = "GET",
            tags = {"BOOK", "ADMIN"},
            summary = "Find all not archived book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "books dont archived"
                    )
            })
    @GetMapping("/not_archived")
    public ResponseEntity<List<Book>> findAllNotArchived(HttpServletRequest req) {
        return ResponseEntity.ok(bookService.findAllNotArchived(req));
    }


    @Operation(
            method = "POST",
            tags = {"BOOK", "ADMIN"},
            summary = "Create new book",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "book created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "this book already exist"
                    )
            })
    @PostMapping()
    public ResponseEntity<Book> create(@RequestBody Book book,HttpServletRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book,req));
    }


    @Operation(
            method = "PUT",
            tags = {"BOOK", "ADMIN"},
            summary = "Create new book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "book updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "book not found"
                    )
            })
    @PutMapping
    public ResponseEntity<Book> update(@RequestBody Book book,HttpServletRequest req) {

        return ResponseEntity.ok(bookService.update(book,req));
    }


    @Operation(
            method = "PUT",
            tags = {"BOOK", "ADMIN"},
            summary = "Delete book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "book archived"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "book not found"
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,HttpServletRequest req) {

        return ResponseEntity.ok(bookService.deleteById(id,req));
    }




}
