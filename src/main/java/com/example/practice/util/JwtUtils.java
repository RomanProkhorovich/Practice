package com.example.practice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.lifetime}")
    private  Duration lifetime;
    @Value("${jwt.secret}")
    private  String secret;

    public String generate(UserDetails userDetails) {
        var now =new Date();
        var exp = new Date(now.getTime() + lifetime.toMillis());
        String jws = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(
                        SignatureAlgorithm.HS256,
                        secret.getBytes()
                )
                .compact();
        return jws;
    }

    public String getUsernameFromToken(String token){
        return getClaims(token).getSubject();
    }


    public String getRoleFromToken(String token){
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token){
        var body=Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return body;
    }


}
