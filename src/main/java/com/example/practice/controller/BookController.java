package com.example.practice.controller;

import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.model.Reader;
import com.example.practice.model.Role;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
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

    @GetMapping
    public ResponseEntity<List<Book>> findAll( Principal userDetails) {
        var reader = readerService.findByEmail(userDetails.getName()).orElseThrow(UserNotFoundException::new);
        Role role = reader.getRole();
        if (role.equals(Role.USER)) {
            return getAllUserBooks(reader);
        }
        else if (role.equals(Role.ADMIN)){
            return getAllBooks();
        }
        throw  new RuntimeException();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable long id){
        var book=bookService.findById(id).orElseThrow(
                ()-> new BookNotFoundException(String.format("Book with id %d not found", id))
        );
        return ResponseEntity.ok(book);
    }
    @GetMapping("/not_archived")
    public ResponseEntity<List<Book>> findAllNotArchived(){
        return ResponseEntity.ok(bookService.findAllNotArchived());
    }

    @PostMapping()
    public ResponseEntity<Book> create(@RequestBody Book book){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
    }

    @PutMapping
    public ResponseEntity<Book> update(@RequestBody Book book){
        return ResponseEntity.ok(bookService.update(book));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        return ResponseEntity.ok( bookService.deleteById(id));
    }


    private ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books;
        books=bookService.findAll();
        return ResponseEntity.ok(books);
    }

    private ResponseEntity<List<Book>> getAllUserBooks(Reader reader) {
        List<Book> books;
        books = logService.findAllBooksByReader(reader);
        return ResponseEntity.ok(books);
    }


}
