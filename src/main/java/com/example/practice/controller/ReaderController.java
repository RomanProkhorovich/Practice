package com.example.practice.controller;

import com.example.practice.exception.IllegalEmailException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import com.example.practice.serviceForController.ReaderServiceForController;
import com.example.practice.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    private final ReaderServiceForController readerService;

    public ReaderController(ReaderServiceForController readerService) {
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
    public ResponseEntity<List<Reader>> findAll(HttpServletRequest req) {
        return ResponseEntity.ok(readerService.findAll(req));
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
    public ResponseEntity<List<Reader>> findAllActive(HttpServletRequest req) {
        return ResponseEntity.ok(readerService.findAllActive(req));
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
    public ResponseEntity<Reader> findByEmail(@PathVariable String email, HttpServletRequest req) {
        var a = readerService.findByEmail(email, req);

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
    public ResponseEntity<Reader> update(@RequestBody Reader reader, HttpServletRequest req) {

        return ResponseEntity.ok(AuthUtil.updateInAuthServer(reader,req));
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
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest req) {

        readerService.deleteById(id, req);
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
    public ResponseEntity<Reader> create(@RequestBody Reader reader, HttpServletRequest req) {

        return ResponseEntity.ok(AuthUtil.saveReaderIntoAuthServer(reader, req));
    }

}


