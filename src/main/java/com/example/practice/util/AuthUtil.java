package com.example.practice.util;

import com.example.practice.exception.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthUtil {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthUtil(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String auth(String username,String password){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );

        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(String.format("User with email %s not found",password));
        }

        return jwtUtils.generateToken(username);
    }
}
