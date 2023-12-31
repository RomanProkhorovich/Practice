package com.example.practice.controller;

import com.example.practice.Dto.MailDto;
import com.example.practice.util.AppMailSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {
    private final AppMailSender mailSender;

    public MailController(AppMailSender mailSender) {
        this.mailSender = mailSender;
    }



    @Operation(
            method = "POST",
            tags = {"MAIL", "ADMIN"},
            summary = "Send email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Send"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody MailDto dto ){
        mailSender.sendMessage(dto.getTo(), dto.getSubject(),dto.getText());
        return ResponseEntity.ok().body("Success");
    }
}
