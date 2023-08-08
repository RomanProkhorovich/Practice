package com.example.practice.controller;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.Token;
import com.example.practice.service.ReaderService;
import com.example.practice.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    public AuthController(JwtUtils jwtUtils, ReaderService readerService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.readerService = readerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<Token> generateToken(@RequestBody AuthDto dto){
        //TODO: badCredetials ex
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword())
        );
        UserDetails user= readerService.loadUserByUsername(dto.getUsername());
        return  ResponseEntity.ok(new Token(jwtUtils.generate(user)));
    }
}
