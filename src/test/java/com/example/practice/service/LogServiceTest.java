package com.example.practice.service;

import com.example.practice.model.*;
import com.example.practice.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc()
@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepository logRepository;

    @Test
    void returnBook() {
    }

    @Test
    void getAllReaderDuty() {
        Reader reader= Reader.builder()
                .email("1@example.ru")
                .firstname("Roma")
                .lastname("")
                .password("123")
                .role(Role.USER)
                .build();
        Book book= new Book("King", Year.of(2010),"IT");
        Mockito.when(logRepository.findAllByReader(reader)).thenReturn( List.of(new Log(book,reader,LocalDate.now().minusDays(100))));
        var a=logService.getAllReaderDuty(reader);

        assertEquals(List.of(new BookDuty(reader,book, 100,0)),a);
    }

    @Test
    void getAllDuty() {
    }
}