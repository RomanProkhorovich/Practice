package com.example.practice.controller;

import com.example.practice.Dto.ExcelDto;
import com.example.practice.service.LogService;
import com.example.practice.util.ExcelFileWriter;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final ExcelFileWriter fileWriter;
    private final LogService logService;

    public ExcelController(ExcelFileWriter fileWriter, LogService logService) {
        this.fileWriter = fileWriter;
        this.logService = logService;
    }

    @PostMapping
    public void generate(@RequestBody ExcelDto dto){
        try {
            ExcelFileWriter.writeCountryListToFile(logService.getAllByMonth(dto.getYear(), Month.of(dto.getMonth())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
