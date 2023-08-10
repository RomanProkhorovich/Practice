package com.example.practice.controller;

import com.example.practice.exception.IllegalEmailException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import com.example.practice.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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



    @Operation(
            method = "POST",
            tags = {"USER", "ADMIN"},
            summary = "get all readers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Reader>> findAll() {
        return ResponseEntity.ok(readerService.findAll());
    }



    @Operation(
            method = "POST",
            tags = {"USER", "ADMIN"},
            summary = "get all active users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found"
                    )
            }
    )

    @GetMapping("/active")
    public ResponseEntity<List<Reader>> findAllActive() {
        return ResponseEntity.ok(readerService.findAllActive());
    }





    @Operation(
            method = "POST",
            tags = {"USER", "ADMIN"},
            summary = "get user by email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "user deleted or not exist"
                    )
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<Reader> findByEmail(@PathVariable String email) {
        var a =readerService.findByEmail(email).orElseThrow(()->new UserNotFoundException(String.format("User with email %s not found", email)));
        if (!a.getIsActive()){
            throw new UserNotFoundException(String.format("User with email %s not found", email));
        }
        return ResponseEntity.ok(a);
    }




    @Operation(
            method = "PUT",
            tags = {"USER", "ADMIN"},
            summary = "update user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "user deleted or not exist"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<Reader> update(@RequestBody Reader reader) {
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));
        return ResponseEntity.ok(readerService.update(reader));
    }


    @Operation(
            method = "PUT",
            tags = {"USER", "ADMIN"},
            summary = "archived user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "deleted"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "user deleted or not exist"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        readerService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }


    @Operation(
            method = "PUT",
            tags = {"USER", "ADMIN"},
            summary = "create user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "user deleted or not exist"
                    )
            }
    )
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
