package com.example.practice.controller;

import com.example.practice.model.Reader;
import com.example.practice.service.ReaderService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ResponseEntity<List<Reader>> findAll() {
        return ResponseEntity.ok(readerService.findAll());
    }


    @GetMapping("/active")
    public ResponseEntity<List<Reader>> findAllActive() {
        return ResponseEntity.ok(readerService.findAllActive());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Reader> findByEmail(@PathVariable String email){
        return ResponseEntity.of(readerService.findByEmail(email));
    }

    @PutMapping
    public ResponseEntity<Reader> update(@RequestBody Reader reader){
        //TODO: validate email
        return ResponseEntity.ok(readerService.update(reader));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id ){
        readerService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping
    public ResponseEntity<Reader> create(@RequestBody Reader reader){
        //TODO: validate email
        return ResponseEntity.ok(readerService.save(reader));
    }

}
