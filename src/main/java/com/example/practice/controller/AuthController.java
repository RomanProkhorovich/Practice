package com.example.practice.controller;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.Dto.Token;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import com.example.practice.service.ReaderService;
import com.example.practice.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final ReaderService readerService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, ReaderService readerService, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.readerService = readerService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
            method = "GET",
            tags = {"TOKEN","USER"},
            summary = "Authentication",
            description = "Authentication by user mail and password",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found"),
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information",required = true)
    )
    @PostMapping
    public ResponseEntity<Token> generateToken(@RequestBody AuthDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(String.format("User with email %s not found", dto.getUsername()));
        }
        UserDetails user = readerService.loadUserByUsername(dto.getUsername());
        return ResponseEntity.ok(new Token(jwtUtils.generateToken(user)));
    }


    @Operation(
            method = "POST",
            tags = {"USER"},
            summary = "Registration",
            description = "Authentication by user mail and password",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found"),
                    @ApiResponse(
                            responseCode = "201",
                            description = "User authenticate successfully"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User mail and password",required = true)
    )
    @PostMapping("/registration")
    public ResponseEntity<AuthDto> doRegistration(@RequestBody RegDto regDto) {
        var password = regDto.getPassword();
        Reader reader = Reader.builder()
                .email(regDto.getEmail())
                .firstname(regDto.getFirstname())
                .lastname(regDto.getLastname())
                .surname(regDto.getSurname())
                .password(passwordEncoder.encode(password))
                .build();
        readerService.save(reader);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthDto(regDto.getEmail(), regDto.getPassword()));
    }
}
