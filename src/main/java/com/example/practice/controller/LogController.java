package com.example.practice.controller;


import com.example.practice.Dto.BookReaderIdDto;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.BookDuty;
import com.example.practice.model.Log;
import com.example.practice.model.Reader;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AppMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    private final BookService bookService;
    private final ReaderService readerService;
    private final AppMailSender mailSender;

    @GetMapping
    public ResponseEntity<List<BookDuty>> findAllDuty() {
        List<BookDuty> allDuty = logService.getAllDuty();
        String text;
        String to;
        String subject = "Просрочка";
        for (var duty : allDuty) {
            to = duty.getReader().getEmail();
            text = "Здравствуйте! Напоминаем о необходимости сдать книгу!\n" +
                    "Название:" + duty.getBook().toString() + " пенни: " + duty.getPenni();

            mailSender.sendMessage(to, subject, text);
        }
        return ResponseEntity.ok(allDuty);

    }

    @GetMapping("/{userid}")
    public ResponseEntity<List<BookDuty>> findAllDutyByReader(@PathVariable Long userid) {
        Reader reader = readerService.findById(userid).orElseThrow(
                () -> new UserNotFoundException(String.format("user with id %d not found", userid)
                ));
        return ResponseEntity.ok(logService.getAllReaderDuty(reader));
    }

    @DeleteMapping
    public ResponseEntity<Log> returnBook(@RequestBody BookReaderIdDto dto) {
        var logId = BookReaderIdDto.map(dto);
        var log = logService.returnBookById(logId);
        return ResponseEntity.ok(log);
    }

    @PostMapping
    public ResponseEntity<Log> issueBook(@RequestBody BookReaderIdDto dto) {
        var reader = readerService.findById(dto.getReaderId()).orElseThrow();
        var book = bookService.findById(dto.getBookId()).orElseThrow();
        return ResponseEntity.ok(logService.save(new Log(book, reader, LocalDate.now())));
    }
}
