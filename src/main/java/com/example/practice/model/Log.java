package com.example.practice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Log {

    @EmbeddedId
    private BookReaderId id=new BookReaderId();

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("readerId")
    @JoinColumn(name = "reader_id")
    private Reader reader;

    private LocalDate issueDate;
    private LocalDate returnedDate;

    public Log(BookReaderId id) {
        this.id = id;
        this.issueDate = LocalDate.now();
    }

    public Log(Book book, Reader reader) {
        this.id.setBookId(book.getId());
        this.id.setReaderId(reader.getId());
        this.book = book;
        this.reader = reader;
        this.issueDate = LocalDate.now();
    }

    public Log() {

    }

}
