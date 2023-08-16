package com.example.practice.service;

import com.example.practice.Dto.BookReaderKeys;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.exception.DeletedUserException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import com.example.practice.util.ExcelFileWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LogService {
    private final LogRepository repository;
    private final BookService bookService;
    private final ReaderService readerService;
    @Value("${library.maxDays}")
    private static int MAX_DAYS;

    public LogService(LogRepository repository, BookService bookService, ReaderService readerService) {
        this.repository = repository;
        this.bookService = bookService;
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
        throw new UsernameNotFoundException("Username not found");
    }
    public List<Book> findAllBooksByReader(String email) {
        var reader = readerService.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return findAllBooksByReader(reader);
    }



    public Log returnBookById(Long id) {
        var log = findById(id).orElseThrow();
        log.setReturnedDate(LocalDate.now());
        return save(log);
    }

    public Log issueBook(BookReaderKeys keys){
        var book=bookService.findById(keys.bookId).orElseThrow(()->new BookNotFoundException());
        var reader=readerService.findById(keys.readerId).orElseThrow(()->new UserNotFoundException());
        return save(new Log(book,reader));
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

    public List<Log> getAllByMonth(Integer year, Month month) {
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

    @Scheduled(fixedRate = 12*60*60*1000)
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
