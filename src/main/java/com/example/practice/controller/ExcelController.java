package com.example.practice.controller;

import com.example.practice.Dto.ExcelDto;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.service.LogService;
import com.example.practice.serviceForController.LogServiceForController;
import com.example.practice.util.ExcelFileWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final LogServiceForController logService;

    public ExcelController( LogServiceForController logService) {
        this.logService = logService;
    }


    @Operation(
            method = "PUT",
            tags = {"EXCEL", "ADMIN"},
            summary = "Create excel",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "excel created"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> generate(@RequestBody ExcelDto dto, HttpServletRequest req) {


        try {
            ExcelFileWriter.writeCountryListToFile(logService.getAllByMonth(dto.getYear(), Month.of(dto.getMonth()),req));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
