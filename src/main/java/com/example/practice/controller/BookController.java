package com.example.practice.controller;

import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.model.Reader;
import com.example.practice.model.Role;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final ReaderService readerService;
    private final LogService logService;

    public BookController(BookService bookService, ReaderService readerService, LogService logService) {
        this.bookService = bookService;
        this.readerService = readerService;
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
    public ResponseEntity<List<Book>> findAll(Principal userDetails) {
        var reader = readerService.findByEmail(userDetails.getName()).orElseThrow(UserNotFoundException::new);
        Role role = reader.getRole();
        if (role.equals(Role.USER)) {
            return ResponseEntity.ok(logService.findAllBooksByReader(reader));
        } else if (role.equals(Role.ADMIN)) {
            return ResponseEntity.ok(bookService.findAll());
        }
        throw new RuntimeException();
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
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        var book = bookService.findById(id).orElseThrow(
                () -> new BookNotFoundException(String.format("Book with id %d not found", id))
        );
        return ResponseEntity.ok(book);
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
    public ResponseEntity<List<Book>> findAllNotArchived() {
        return ResponseEntity.ok(bookService.findAllNotArchived());
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
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
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
    public ResponseEntity<Book> update(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.update(book));
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.deleteById(id));
    }


}
