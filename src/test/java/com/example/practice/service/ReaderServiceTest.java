package com.example.practice.service;

import com.example.practice.Dto.RegDto;
import com.example.practice.exception.UserAlreadyExistException;
import com.example.practice.exception.UserNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.model.Reader;
import com.example.practice.model.Role;
import com.example.practice.repository.ReaderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ReaderRepository readerRepository;
    @InjectMocks
    ReaderService readerService;

    @Test
    void save() {

        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        Mockito.when(readerRepository.save(reader2)).thenReturn((reader2));
        assertEquals(readerService.save(reader2), reader2);
    }

    @Test
    void saveExpectedException() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        Mockito.when(readerRepository.findByEmail("2@example.com")).thenReturn(Optional.ofNullable(reader2));
        //Mockito.when(readerRepository.save(reader2)).thenReturn((reader2));
        assertThrows(UserAlreadyExistException.class, () -> readerService.save(reader2));

    }

    @Test
    void findByEmail() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        Mockito.when(readerRepository.findByEmail("2@example.com")).thenReturn(Optional.ofNullable(reader2));
        assertEquals(reader2, readerService.findByEmail("2@example.com").get());
    }

    @Test
    void findByEmailExpectException() {
        assertEquals(Optional.empty(), readerService.findByEmail("123@mail.com"));
    }

    @Test
    void findById() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader2 = Reader.builder()
                .id(1L)
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.ofNullable(reader2));
        assertEquals(reader2, readerService.findById(1L).get());
    }

    @Test
    void findByIdExpectNull() {
        assertEquals(Optional.empty(), readerService.findById(4L));
    }

    @Test
    void findAll() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader = Reader.builder()
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(true)
                .build();

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();
        Mockito.when(readerRepository.findAll()).thenReturn(List.of(reader, reader2));

        assertEquals(2, readerService.findAll().size());
    }


    @Test
    void findAllActive() {

        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader = Reader.builder()
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(false)
                .build();

        Reader reader2 = Reader.builder()
                .email("2@example.com")
                .role(Role.USER)
                .password(passwordEncoder.encode("123"))
                .firstname("Andrey")
                .lastname("Andreev")
                .isActive(true)
                .build();

        Mockito.when(readerRepository.findAllByIsActive(true)).thenReturn(List.of(reader2));

        assertEquals(1, readerService.findAllActive().size());

    }

    @Test
    void update() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader = Reader.builder()
                .id(1L)
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(false)
                .build();
        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        Reader updatedReader = Reader.builder()
                .id(1L)
                .email("1231244@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(false)
                .build();
        Mockito.when(readerRepository.save(reader)).thenReturn((updatedReader));
        assertEquals(updatedReader, readerService.update(reader));
    }


    @Test
    void deleteById() {
        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader = Reader.builder()
                .id(1L)
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(false)
                .build();
        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        assertEquals(reader, readerService.findById(1L).get());

    }

    @Test
    void deleteByIdExpectException() {

        assertThrows(UserNotFoundException.class, () -> readerService.deleteById(1L));
    }

    @Test
    void deleteByEmail() {

        Mockito.when(passwordEncoder.encode("123")).thenReturn("123");

        Reader reader = Reader.builder()
                .id(1L)
                .email("1@example.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .firstname("Ivan")
                .lastname("Ivanov")
                .isActive(false)
                .build();
        Mockito.when(readerRepository.findByEmail("1@example.com")).thenReturn(Optional.of(reader));

        assertEquals(reader, readerService.findByEmail("1@example.com").get());
    }

    @Test
    void deleteByEmailExpectException() {

        assertThrows(UserNotFoundException.class, () -> readerService.deleteByEmail("121@example.com"));
    }

}