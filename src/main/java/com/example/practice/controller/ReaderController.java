package com.example.practice.controller;

import com.example.practice.exception.IllegalEmailException;
import com.example.practice.model.Reader;
import com.example.practice.service.ReaderService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    private final ReaderService readerService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ReaderController(ReaderService readerService, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<Reader> findByEmail(@PathVariable String email) {
        return ResponseEntity.of(readerService.findByEmail(email));
    }

    @PutMapping
    public ResponseEntity<Reader> update(@RequestBody Reader reader) {
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));
        return ResponseEntity.ok(readerService.update(reader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        readerService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping
    public ResponseEntity<Reader> create(@RequestBody Reader reader) {
        validateEmail(reader.getEmail());
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(readerService.save(reader));
    }

    private void validateEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalEmailException(String.format("Email %s is invalid", email));
        }
    }

}
