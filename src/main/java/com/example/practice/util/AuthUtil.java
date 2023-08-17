package com.example.practice.util;

import com.example.practice.Dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AuthUtil {
    public static Response getRolesFromAuthServer(HttpServletRequest req){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", req.getHeader("Authorization") );
        HttpEntity<?> request = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange("http://localhost:8081/api/readers",
                HttpMethod.GET,
                request,
                Response.class);
        return response.getBody();
    }
}
