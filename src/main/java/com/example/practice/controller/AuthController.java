package com.example.practice.controller;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.Dto.Token;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AuthUtil;
import com.example.practice.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthUtil authUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, ReaderService readerService, AuthUtil authUtil, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.readerService = readerService;
        this.authUtil = authUtil;
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

        authUtil.auth(dto.getUsername(),dto.getPassword());
        return ResponseEntity.ok(new Token(jwtUtils.generateToken(dto.getUsername())));
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

        readerService.save(regDto,passwordEncoder);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthDto(regDto.getEmail(), regDto.getPassword()));
    }
}
