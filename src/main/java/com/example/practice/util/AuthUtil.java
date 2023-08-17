package com.example.practice.util;

import com.example.practice.Dto.AuthDto;
import com.example.practice.Dto.RegDto;
import com.example.practice.Dto.Response;
import com.example.practice.Dto.Token;
import com.example.practice.exception.IllegalEmailException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Reader;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AuthUtil {
    public static Response getRolesFromAuthServer(HttpServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", req.getHeader("Authorization"));
        HttpEntity<?> request = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange("http://localhost:8081/api/readers",
                HttpMethod.GET,
                request,
                Response.class);
        return response.getBody();
    }

    public static void checkAdminRole(HttpServletRequest req) {
        var res = getRolesFromAuthServer(req);
        if (!res.isAuthenticated() || !res.getRole().equals("ADMIN"))
            throw new UserNotFoundException();
    }

    public static AuthDto doReg(RegDto regDto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<RegDto> request = new HttpEntity<>(regDto);
        return restTemplate.postForObject("http://localhost:8081/auth/registration", request, AuthDto.class);
    }

    public static Reader saveReaderIntoAuthServer(Reader reader, HttpServletRequest req) {
        validateEmail(reader.getEmail());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", req.getHeader("Authorization"));
        HttpEntity<Reader> request = new HttpEntity<Reader>(reader,
                headers);
        return restTemplate.postForObject("http://localhost:8081/api/readers", request, Reader.class);
    }

    public static Token generateToken(AuthDto dto) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<AuthDto> request = new HttpEntity<>(dto);
        return restTemplate.postForObject("http://localhost:8081/auth", request, Token.class);
    }

    public static Reader updateInAuthServer(Reader reader,HttpServletRequest req){
        validateEmail(reader.getEmail());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", req.getHeader("Authorization"));
        HttpEntity<Reader> request = new HttpEntity<>(reader, headers);
        HttpEntity<Reader> response = restTemplate.exchange("http://localhost:8081/api/readers",
                HttpMethod.PUT,
                request,
                Reader.class);
        return response.getBody();
    }

    private static void validateEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalEmailException(String.format("Email %s is invalid", email));
        }
    }
}



