package com.example.practice.config;

import com.example.practice.model.*;
import com.example.practice.service.BookService;
import com.example.practice.service.LogService;
import com.example.practice.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Year;

@Configuration
@RequiredArgsConstructor
public class DBConfig {
    private final BCryptPasswordEncoder passwordEncoder;

    private final BookService bookService;

    private final ReaderService readerService;
    private final LogService logService;
    @Bean
    public void init(){
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");

        it=bookService.save(it);
        prideAndPrejudice=bookService.save(prideAndPrejudice);
        winnie=bookService.save(winnie);
        lordOfTheRing=bookService.save(lordOfTheRing);

        Reader reader = Reader.builder()
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(true)
                .build();
        reader= readerService.save(reader);

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        reader2= readerService.save(reader2);

        // Log log= logService.save(new Log(it,reader2, LocalDate.now()));


    }
}
