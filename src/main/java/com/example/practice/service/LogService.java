package com.example.practice.service;

import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LogService {
    private final LogRepository repository;
    @Value("${library.maxDays}")
    private static int MAX_DAYS;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public Log save(Log log) {
        return repository.save(log);
    }


    public Optional<Log> findById(BookReaderId id) {
        return repository.findById(id);
    }

    public List<Book> findAllBooksByReader(Reader reader) {
        return repository.findAllByReader(reader).stream()
                .map(Log::getBook)
                .collect(Collectors.toList());
    }

    public Log returnBook(Reader reader, Book book) {
        return returnBookById(new BookReaderId(book.getId(),reader.getId()));

    }
    public Log returnBookById(BookReaderId id) {
        var log = findById(id).orElseThrow();
        log.setReturnedDate(LocalDate.now());
        return save(log);
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

    public List<Log> getAllByMonth(Integer year, Month month){
        var start=LocalDate.of(year, month,1);
        var end=LocalDate.of(year, month,31);
        return repository.findAllByIssueDateBetween(start,end);
    }

    public List<BookDuty> getAllDuty() {
        return repository.findAllByIssueDateBeforeAndReturnedDate(LocalDate.now().minusDays(MAX_DAYS), null)
                .stream()
                .map(x -> new BookDuty(x.getReader(), x.getBook(),
                        getDurationBetweenNowAndIssueDate(x.getIssueDate())))
                .collect(Collectors.toList());
    }

    private static long getDurationBetweenNowAndIssueDate(LocalDate issue) {
        return Math.abs(Duration.ofDays(DAYS.between(LocalDate.now(), issue)).toDays());
    }

    public static boolean checkIsDuty(Log log){
        return  (log.getIssueDate().isBefore(LocalDate.now().minusDays(MAX_DAYS)) &&
                log.getReturnedDate() == null);
    }
}
