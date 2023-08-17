package com.example.practice.controller;


import com.example.practice.Dto.BookReaderKeys;
import com.example.practice.model.BookDuty;
import com.example.practice.model.Log;
import com.example.practice.serviceForController.LogServiceForController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogServiceForController logService;

    public LogController(LogServiceForController logService) {
        this.logService = logService;
    }


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
