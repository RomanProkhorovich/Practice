package com.example.practice.controller;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.Dto.Token;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.example.practice.util.AuthUtil.doReg;

@RestController
@RequestMapping("/auth")
public class AuthController {



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

        return ResponseEntity.ok(AuthUtil.generateToken(dto));
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
        return ResponseEntity.ok(doReg(regDto));
    }
}
