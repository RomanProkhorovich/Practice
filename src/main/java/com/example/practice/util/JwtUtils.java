package com.example.practice.util;

import com.example.practice.service.ReaderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtils {
    private final ReaderService readerService;

    @Value("${jwt.lifetime}")
    private  Duration lifetime;
    @Value("${jwt.secret}")
    private  String secret;

    public JwtUtils(ReaderService readerService) {
        this.readerService = readerService;
    }

    private byte[] getSecret(){
        return secret.getBytes();
    }

    public String generateToken(String username){
        return generateToken(readerService.loadUserByUsername(username));
    }
    public String generateToken(UserDetails details) {

        String role = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).findFirst().orElseThrow();


        Date created = new Date();
        Date expired = new Date(created.getTime() + lifetime.toMillis());
        return Jwts.builder()
                .claim("roles",role)
                .setIssuedAt(created)
                .setExpiration(expired)
                .setSubject(details.getUsername())
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String getRoleFromToken(String token){
        return getClaimsFromToken(token).get("roles",String.class);
    }

    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}



