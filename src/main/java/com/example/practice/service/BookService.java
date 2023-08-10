package com.example.practice.service;

import com.example.practice.exception.BookAlreadyExist;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {


    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        if (findByAuthorNameAndReleasedAtAndTitle(book.getAuthorName(), book.getReleasedAt(),book.getTitle()).isPresent()) {
            throw new BookAlreadyExist(
                    String.format("Book with author: '%s' , released at: '%s', title: '%s' already exist",
                            book.getAuthorName(),
                            book.getReleasedAt(),
                            book.getTitle()));
        }

        return bookRepository.save(book);
    }
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public List<Book> findAllNotArchived(){
        return bookRepository.findAll().
                stream()
                .filter(x-> !x.getArchived())
                .collect(Collectors.toList());
    }
    public Book update(Book book){
        var updated =findById(book.getId())
                .orElseThrow(()-> new BookNotFoundException(String.format("Book with id: '%d' not found", book.getId())));

        if (!book.getAuthorName().isBlank())
            updated.setAuthorName(book.getAuthorName());
        if (book.getReleasedAt().isBefore(Year.now()))
            updated.setReleasedAt(book.getReleasedAt());
        if (!book.getTitle().isBlank())
            updated.setTitle(book.getTitle());

        return save(updated);
    }

    public Book deleteById(Long id){
        Book book = findById(id).orElseThrow(() -> new BookNotFoundException(String.format("Book with id: '%d' not found", id)));
        book.setArchived(true);
        return book;
    }

    private Optional<Book> findByAuthorNameAndReleasedAtAndTitle(String author, Year releaseAt, String title){
        return bookRepository.findByAuthorNameAndReleasedAtAndTitle(author,releaseAt,title);
    }
}
