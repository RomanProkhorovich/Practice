package com.example.practice.controller;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.Dto.Token;
import com.example.practice.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ReaderService readerService;

    public AuthController(ReaderService readerService) {
        this.readerService = readerService;
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
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<AuthDto> request = new HttpEntity<>(dto);
        Token foo = restTemplate.postForObject("http://localhost:8081/auth", request, Token.class);
        return ResponseEntity.ok(foo);
      /*  return ResponseEntity.ok(new Token( authUtil.auth(dto.getUsername(),dto.getPassword())));*/
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
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<RegDto> request = new HttpEntity<>(regDto);
        AuthDto foo = restTemplate.postForObject("http://localhost:8081/auth/registration", request, AuthDto.class);
        return ResponseEntity.ok(foo);
        //return ResponseEntity.status(HttpStatus.CREATED).body(readerService.saveAndMap(regDto,passwordEncoder));
    }
}
