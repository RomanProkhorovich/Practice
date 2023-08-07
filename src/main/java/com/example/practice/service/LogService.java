package com.example.practice.service;

import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LogService {
    private final LogRepository repository;
    @Value("${library.maxDays}")
    private int MAX_DAYS;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public Log save(Log log){
        return repository.save(log);
    }

    public Optional<Log> findById(BookReaderId id) {
        return repository.findById(id);
    }
    public List<Book> findAllBooksByReader(Reader reader){
        return repository.findAllByReader(reader).stream()
                .map(Log::getBook)
                .collect(Collectors.toList());
    }

    public Log returnBook(Reader reader, Book book) {
        var log = findById(new BookReaderId(book.getId(), reader.getId())).orElseThrow();
        log.setReturnedDate(LocalDate.now());
        return  save(log);
    }


    //TODO: расчитать пенни
    public List<BookDuty> getAllReaderDuty(Reader reader){
       return repository.findAllByReader(reader).stream()
               .filter(x->x.getIssueDate().isBefore(LocalDate.now().minusDays(MAX_DAYS)))
               .map(x-> new BookDuty(x.getReader(),
                       x.getBook(),
                       getDurationBetweenNonAndIssueDate(x.getIssueDate())
                       ,0))
               .collect(Collectors.toList());
    }
    public List<BookDuty> getAllDuty(){
        return repository.findAllByIssueDateBefore(LocalDate.now().minusDays(MAX_DAYS))
                .stream()
                .map(x-> new BookDuty(x.getReader(),x.getBook(),
                        getDurationBetweenNonAndIssueDate(x.getIssueDate())
                        ,0))
                .collect(Collectors.toList());
    }

    private static long getDurationBetweenNonAndIssueDate(LocalDate issue) {
        return Math.abs(Duration.ofDays(DAYS.between(LocalDate.now(), issue)).toDays());
    }
}
