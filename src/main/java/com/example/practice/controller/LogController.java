package com.example.practice.controller;


import com.example.practice.Dto.BookReaderKeys;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.BookDuty;
import com.example.practice.model.Log;
import com.example.practice.model.Reader;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AppMailSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    private final ReaderService readerService;
    private final AppMailSender mailSender;


    @Operation(
            method = "GET",
            tags = {"DUTY", "ADMIN"},
            summary = "All duty",
            description = "find all duty and send email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BookDuty>> findAllDuty(HttpServletRequest req) {

        return ResponseEntity.ok(logService.getAllAndSendEmail(req));

    }



    @Operation(
            method = "GET",
            tags = {"DUTY", "ADMIN"},
            summary = "All reader duty",
            description = "find all duty by reader and send email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found"
                    )
            }
    )
    @GetMapping("/{userid}")
    public ResponseEntity<List<BookDuty>> findAllDutyByReader(@PathVariable Long userid,HttpServletRequest req) {
        return ResponseEntity.ok(logService.getAllReaderDutyAndSendEmail(userid,req));
    }


    @Operation(
            method = "DELETE",
            tags = {"DUTY", "ADMIN"},
            summary = "return book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "returned"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "log not found"
                    )
            }
    )
    @DeleteMapping
    public ResponseEntity<Log> returnBook(@RequestBody long id, HttpServletRequest req) {

        var log = logService.returnBookById(id,req);
        return ResponseEntity.ok(log);
    }


    @Operation(
            method = "POST",
            tags = {"DUTY", "ADMIN"},
            summary = "issue book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "issued"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "book or user deleted or not exist"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Log> issueBook(@RequestBody BookReaderKeys dto, HttpServletRequest req) {

        return ResponseEntity.ok(logService.issueBook(dto,req));
    }

}
