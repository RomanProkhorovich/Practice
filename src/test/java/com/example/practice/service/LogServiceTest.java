package com.example.practice.service;

import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {
    @Mock
    LogRepository repository;
    @Mock
    BookService bookService;
    @Mock
    ReaderService readerService;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    LogService logService;

    @BeforeAll
    static void iitDays(){
        ReflectionTestUtils.setField(LogService.class, "MAX_DAYS",30);
    }
    @Test
    void save() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");
        Book it= new Book(1L,"Stephen King", Year.of(1986), "IT",false);

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log2= (new Log(it, reader2));
        Mockito.when(repository.save(log2)).thenReturn(log2);
        Mockito.when(readerService.findById(1L)).thenReturn(Optional.of(reader2));
        Mockito.when(bookService.findById(1L)).thenReturn(Optional.of(it));

        assertEquals(log2,logService.save(log2));
    }

    @Test
    void findById() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");
        Book it= new Book(1L,"Stephen King", Year.of(1986), "IT",false);

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log2= (new Log(it, reader2));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(log2));

        assertEquals(log2,logService.findById(1L).get());
    }

    @Test
    void findAllBooksByReader() {

        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log4= new Log(lordOfTheRing, reader2);
        Log log3= new Log(winnie, reader2);
        Log log= new Log(prideAndPrejudice, reader2);
        Log log2= (new Log(it, reader2));

        var books = List.of(it, prideAndPrejudice, winnie, lordOfTheRing);
        Mockito.when(repository.findAllByReader(reader2)).thenReturn(List.of(log,log2,log3,log4));

        assertEquals(4,logService.findAllBooksByReader(reader2).size());

    }


    @Test
    void returnBookById() {
        Book prideAndPrejudice= new Book(1L,"Jane Austen", Year.of(1813), "Pride and Prejudice",false);
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();

        Log log= new Log(prideAndPrejudice, reader2);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(log));
        Mockito.when(readerService.findById(1L)).thenReturn(Optional.of(reader2));
        Mockito.when(bookService.findById(1L)).thenReturn(Optional.of(prideAndPrejudice));
        logService.returnBookById(1L);
        assertNotEquals(null,logService.findById(1L).get().getReturnedDate());
    }

    @Test
    void getAllReaderDuty() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log4= new Log(lordOfTheRing, reader2);
        log4.setIssueDate(LocalDate.MIN);

        Log log3= new Log(winnie, reader2);
        log3.setIssueDate(LocalDate.MIN);

        Log log= new Log(prideAndPrejudice, reader2);
        log.setIssueDate(LocalDate.MIN);

        Log log2= (new Log(it, reader2));
        log2.setIssueDate(LocalDate.MIN);

        var books = List.of(it, prideAndPrejudice, winnie, lordOfTheRing);
        Mockito.when(repository.findAllByReader(reader2)).thenReturn(List.of(log,log2,log3,log4));

        assertEquals(4,logService.getAllReaderDuty(reader2).size());
    }

    @Test
    void getAllByMonth() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log4= new Log(lordOfTheRing, reader2);
        log4.setIssueDate(LocalDate.MIN);

        Log log3= new Log(winnie, reader2);
        log3.setIssueDate(LocalDate.MIN);

        Log log= new Log(prideAndPrejudice, reader2);
        log.setIssueDate(LocalDate.MIN);

        Log log2= (new Log(it, reader2));
        log2.setIssueDate(LocalDate.MIN);

        var books = List.of(it, prideAndPrejudice, winnie, lordOfTheRing);
        var start=LocalDate.of(-999999999,1,1);
        var end=LocalDate.of(-999999999, 1, start.lengthOfMonth());
        Mockito.when(repository.findAllByIssueDateBetween(start,end)).thenReturn(List.of(log,log2,log3,log4));

        List<Log> allByMonth = logService.getAllByMonth(-999999999, Month.of(1));
        int size = allByMonth.size();
        assertEquals(4, size);
    }

    @Test
    void getAllDuty() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");
        Mockito.when(passwordEncoder.encode("123")).thenReturn("`123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();


        Log log4= new Log(lordOfTheRing, reader2);
        log4.setIssueDate(LocalDate.MIN);

        Log log3= new Log(winnie, reader2);
        log3.setIssueDate(LocalDate.MIN);

        Log log= new Log(prideAndPrejudice, reader2);
        log.setIssueDate(LocalDate.MIN);

        Log log2= (new Log(it, reader2));
        log2.setIssueDate(LocalDate.MIN);

        var books = List.of(it, prideAndPrejudice, winnie, lordOfTheRing);
        Mockito.when(repository.findAllByIssueDateBeforeAndReturnedDate(LocalDate.now().minusDays(30), null)).thenReturn(List.of(log,log2,log3,log4));

        assertEquals(4,logService.getAllDuty().size());
    }


}