package com.example.practice.serviceForController;

import com.example.practice.Dto.BookReaderKeys;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.DeletedUserException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import com.example.practice.util.AppMailSender;
import com.example.practice.util.AuthUtil;
import com.example.practice.util.ExcelFileWriter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.practice.util.AuthUtil.getRolesFromAuthServer;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LogServiceForController {
    private final LogRepository repository;
    private final AppMailSender mailSender;
    private final ReaderService readerService;
    private final LogService logService;

    public LogServiceForController(LogRepository repository, AppMailSender mailSender, ReaderService readerService, LogService logService) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.readerService = readerService;
        this.logService = logService;
    }


    public Log save(Log log) {
        var reader = log.getReader();

        if (!reader.getIsActive()) {
            throw new DeletedUserException(String.format("Reader with id %d is deleted", reader.getId()));
        }

        var book = log.getBook();
        if (book.getArchived()) {
            throw new BookNotFoundException(String.format("Book with id %d is archived", book.getId()));
        }
        return repository.save(log);
    }



    public Log returnBookById(Long id, HttpServletRequest req) {
        AuthUtil.getRolesFromAuthServer(req);
        return logService.returnBookById(id);
    }
    public List<Book> findAllBooksByReader(String email,HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return logService.findAllBooksByReader(email);
    }

    public Log issueBook(BookReaderKeys keys, HttpServletRequest req) {
        AuthUtil.getRolesFromAuthServer(req);
        return logService.issueBook(keys);
    }


    public List<BookDuty> getAllReaderDutyAndSendEmail(Long id, HttpServletRequest req) {
        var res = getRolesFromAuthServer(req);
        if (!res.isAuthenticated())
            throw new UserNotFoundException();
        Reader reader = readerService.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("user with id %d not found", id)
                ));
        var allDuty = logService.getAllReaderDuty(reader);
        String text;
        String to;
        String subject = "Просрочка";
        for (var duty : allDuty) {
            to = duty.getReader().getEmail();
            text = "Здравствуйте! Напоминаем о необходимости сдать книгу!\n" +
                    "Название:" + duty.getBook().toString() + " пенни: " + duty.getPenni();

            mailSender.sendMessage(to, subject, text, req);
        }
        return allDuty;
    }

    public List<Log> getAllByMonth(Integer year, Month month, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return logService.getAllByMonth(year, month);
    }


    public List<BookDuty> getAllAndSendEmail(HttpServletRequest req) {
        var res = getRolesFromAuthServer(req);
        if (!res.isAuthenticated() || !res.getRole().equals("ADMIN"))
            throw new UserNotFoundException();
        List<BookDuty> allDuty = logService.getAllDuty();
        String text;
        String to;
        String subject = "Просрочка";
        for (var duty : allDuty) {
            to = duty.getReader().getEmail();
            text = "Здравствуйте! Напоминаем о необходимости сдать книгу!\n" +
                    "Название:" + duty.getBook().toString() + " пенни: " + duty.getPenni();

            mailSender.sendMessage(to, subject, text, req);
        }
        return allDuty;
    }

}
