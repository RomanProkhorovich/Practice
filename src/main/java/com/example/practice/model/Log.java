package com.example.practice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "fk_reader_book",columnNames = {"reader_id","book_id"}))
public class Log {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate issueDate;
    private LocalDate returnedDate;


    public Log(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
        this.issueDate = LocalDate.now();
    }

    public Log() {
        this.issueDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(reader, log.reader) && Objects.equals(book, log.book) && Objects.equals(issueDate, log.issueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, book, issueDate);
    }
}
