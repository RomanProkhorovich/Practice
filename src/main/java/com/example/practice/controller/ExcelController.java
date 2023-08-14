package com.example.practice.controller;

import com.example.practice.Dto.ExcelDto;
import com.example.practice.service.LogService;
import com.example.practice.util.ExcelFileWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final LogService logService;

    public ExcelController( LogService logService) {
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
    public ResponseEntity<?> generate(@RequestBody ExcelDto dto) {
        try {
            ExcelFileWriter.writeCountryListToFile(logService.getAllByMonth(dto.getYear(), Month.of(dto.getMonth())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
