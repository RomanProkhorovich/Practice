package com.example.practice.service;

import com.example.practice.exception.BookAlreadyExist;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.repository.BookRepository;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
    @Test
    void saveExpectSave() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");


        //Mockito.when(bookRepository.findByAuthorNameAndReleasedAtAndTitle("Stephen King", Year.of(1986), "IT")).thenReturn(Optional.of(it));
        Mockito.when(bookRepository.save(it)).thenReturn(it);
        assertEquals(it,bookService.save(it));
    }

    @Test
    void saveExpectException() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");

        Mockito.when(bookRepository.findByAuthorNameAndReleasedAtAndTitle("Stephen King", Year.of(1986), "IT")).thenReturn(Optional.of(it));

        assertThrows(BookAlreadyExist.class,()->bookService.save(it));
    }


    @Test
    void findById() {
        Book it= new Book(1L, "Stephen King", Year.of(1986), "IT",false);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(it));
        assertEquals(it,bookService.findById(it.getId()).get());
    }

    @Test
    void findByIdExpectNull() {

        assertEquals(bookService.findById(2L),Optional.empty());
    }

    @Test
    void findAll() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Book prideAndPrejudice= new Book("Jane Austen", Year.of(1813), "Pride and Prejudice");
        Book winnie= new Book("Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh");
        Book lordOfTheRing= new Book("John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings");
        List<Book> of = List.of(it,prideAndPrejudice,winnie,lordOfTheRing);
        Mockito.when(bookRepository.findAll()).thenReturn(of);
        assertEquals(bookService.findAll().size(),4);
    }

    @Test
    void findAllNotArchived() {
        Book it= new Book(1L,"Stephen King", Year.of(1986), "IT",false);
        Book prideAndPrejudice= new Book(2L,"Jane Austen", Year.of(1813), "Pride and Prejudice",true);
        Book winnie= new Book(3L,"Alan Alexander Milne", Year.of(1926), "Winnie-the-Pooh",false);
        Book lordOfTheRing= new Book(4L,"John Ronald Reuel Tolkien", Year.of(1955), "The Lord of the Rings",true);
        List<Book> of = List.of(it,prideAndPrejudice,winnie,lordOfTheRing);

        Mockito.when(bookRepository.findAllByArchived(false)).thenReturn(List.of(it,winnie));
        assertEquals(bookService.findAllNotArchived().size(),2);
    }

    @Test
    void update() {
        Book it= new Book(1L,"Stephen King", Year.of(1986), "IT",false);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(it));

        Book updatedIt = new Book("Stephen King", Year.of(2000), "IT2");

        Mockito.when(bookRepository.save(it)).thenReturn((updatedIt));
        assertEquals(updatedIt, bookService.update(it));
    }

    @Test
    void updateExpectException() {
        Book it= new Book(1L,"Stephen King", Year.of(1986), "IT",false);
        assertThrows(BookNotFoundException.class , ()-> bookService.update(it));
    }

    @Test
    void deleteById() {
        Book it= new Book("Stephen King", Year.of(1986), "IT");
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(it));
        assertEquals(bookService.deleteById(1L).getArchived(),true);
    }
    @Test
    void deleteByIdExpectException() {
        assertThrows(BookNotFoundException.class , ()-> bookService.deleteById(1L));
    }
}