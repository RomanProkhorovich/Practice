package com.example.practice.serviceForController;

import com.example.practice.exception.BookAlreadyExist;
import com.example.practice.exception.BookNotFoundException;
import com.example.practice.model.Book;
import com.example.practice.repository.BookRepository;
import com.example.practice.service.BookService;
import com.example.practice.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static com.example.practice.util.AuthUtil.checkAdminRole;

@Service
public class BookServiceForController {

    private final BookRepository bookRepository;
    private final BookService bookService;

    public BookServiceForController(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    public Book save(Book book, HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return bookService.save(book);
    }

    public Book findById(Long id,HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return bookRepository.findById(id).orElseThrow(()->new BookNotFoundException());
    }

    public List<Book> findAll(HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return bookRepository.findAll();
    }

    public List<Book> findAllNotArchived(HttpServletRequest req) {
        AuthUtil.checkAdminRole(req);
        return bookService.findAllNotArchived();
    }

    public Book update(Book book, HttpServletRequest req) {
        checkAdminRole(req);
        return bookService.update(book);
    }

    public Book deleteById(Long id, HttpServletRequest req) {
        checkAdminRole(req);
        return bookService.deleteById(id);
    }

    private Optional<Book> findByAuthorNameAndReleasedAtAndTitle(String author, Year releaseAt, String title) {
        return bookRepository.findByAuthorNameAndReleasedAtAndTitle(author, releaseAt, title);
    }
}
