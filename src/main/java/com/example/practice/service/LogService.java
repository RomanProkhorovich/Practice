package com.example.practice.service;

import com.example.practice.Dto.BookReaderKeys;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.DeletedUserException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
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
public class LogService {
    private final LogRepository repository;
    private final BookService bookService;
    private final AppMailSender mailSender;
    private final ReaderService readerService;
    @Value("${library.maxDays}")
    private static int MAX_DAYS;


    public LogService(LogRepository repository, BookService bookService, AppMailSender mailSender, ReaderService readerService) {
        this.repository = repository;
        this.bookService = bookService;
        this.mailSender = mailSender;
        this.readerService = readerService;
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


    public Optional<Log> findById(Long id) {
        return repository.findById(id);
    }


    public List<Book> findAllBooksByReader(Reader reader) {
        if (reader.getRole().equals(Role.USER))
            return repository.findAllByReader(reader).stream()
                    .filter(x -> x.getReturnedDate() == null)
                    .map(Log::getBook)
                    .collect(Collectors.toList());
        else if (reader.getRole().equals(Role.ADMIN)) {
            return bookService.findAll();
        }
        throw new UserNotFoundException("Username not found");
    }
    public List<Book> findAllBooksByReader(String email,HttpServletRequest req) {
        var reader = readerService.findByEmail(email,req);
        return findAllBooksByReader(reader);
    }



    public Log returnBookById(Long id,HttpServletRequest req) {
        AuthUtil.getRolesFromAuthServer(req);
        var log = findById(id).orElseThrow();
        log.setReturnedDate(LocalDate.now());
        return save(log);
    }

    public Log issueBook(BookReaderKeys keys,HttpServletRequest req){
        AuthUtil.getRolesFromAuthServer(req);
        var book=bookService.findById(keys.bookId).orElseThrow(()->new BookNotFoundException());
        var reader=readerService.findById(keys.readerId).orElseThrow(()->new UserNotFoundException());
        return save(new Log(book,reader));
    }


    public List<BookDuty> getAllReaderDuty(Long id){
        return getAllReaderDuty(readerService.findById(id).orElseThrow(()->new UserNotFoundException()));
    }
    public List<BookDuty> getAllReaderDuty(Reader reader) {
        var notReturned = repository.findAllByReader(reader).stream()
                .filter(LogService::checkIsDuty).collect(Collectors.toSet());

        return notReturned.stream()
                .map(x -> new BookDuty(x.getReader(),
                        x.getBook(),
                        getDurationBetweenNowAndIssueDate(x.getIssueDate())))
                .collect(Collectors.toList());
    }
    public List<BookDuty> getAllReaderDutyAndSendEmail(Long id,HttpServletRequest req){
        var res=getRolesFromAuthServer(req);
        if (!res.isAuthenticated())
            throw new UserNotFoundException();
        Reader reader = readerService.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("user with id %d not found", id)
                ));
        var allDuty=getAllReaderDuty(reader);
        String text;
        String to;
        String subject = "Просрочка";
        for (var duty : allDuty) {
            to = duty.getReader().getEmail();
            text = "Здравствуйте! Напоминаем о необходимости сдать книгу!\n" +
                    "Название:" + duty.getBook().toString() + " пенни: " + duty.getPenni();

            mailSender.sendMessage(to, subject, text,req);
        }
        return allDuty;
    }

    public List<Log> getAllByMonth(Integer year, Month month, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        var start = LocalDate.of(year, month, 1);
        var end = LocalDate.of(year, month, start.lengthOfMonth());
        return repository.findAllByIssueDateBetween(start, end);
    }

    public List<BookDuty> getAllDuty() {
        return repository.findAllByIssueDateBeforeAndReturnedDate(LocalDate.now().minusDays(MAX_DAYS), null)
                .stream()
                .map(x -> new BookDuty(x.getReader(), x.getBook(),
                        getDurationBetweenNowAndIssueDate(x.getIssueDate())))
                .collect(Collectors.toList());
    }

    public List<BookDuty> getAllAndSendEmail(HttpServletRequest req){
        var res=getRolesFromAuthServer(req);
        if (!res.isAuthenticated()|| !res.getRole().equals("ADMIN"))
            throw new UserNotFoundException();
        List<BookDuty> allDuty = getAllDuty();
        String text;
        String to;
        String subject = "Просрочка";
        for (var duty : allDuty) {
            to = duty.getReader().getEmail();
            text = "Здравствуйте! Напоминаем о необходимости сдать книгу!\n" +
                    "Название:" + duty.getBook().toString() + " пенни: " + duty.getPenni();

            mailSender.sendMessage(to, subject, text,req);
        }
        return allDuty;
    }

    @Scheduled(fixedDelayString = "${delay}")
    @Async
    public void generateLog() throws IOException {
        ExcelFileWriter.generateSchedulerFile(repository.findAllByReturnedDate(null));

    }

    private static long getDurationBetweenNowAndIssueDate(LocalDate issue) {
        return Math.abs(Duration.ofDays(DAYS.between(LocalDate.now(), issue)).toDays());
    }

    public static boolean checkIsDuty(Log log) {
        return (log.getIssueDate().isBefore(LocalDate.now().minusDays(MAX_DAYS)) &&
                log.getReturnedDate() == null);
    }
}
