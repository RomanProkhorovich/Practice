package com.example.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
public class BookDuty {
    private Reader reader;
    private Book book;
    private long days;
    private float penni;

    public BookDuty(Reader reader, Book book, long days, float penni) {
        this.reader = reader;
        this.book = book;
        this.days = days;
        this.penni = penni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDuty bookDuty = (BookDuty) o;
        return days == bookDuty.days && Float.compare(bookDuty.penni, penni) == 0 && Objects.equals(reader, bookDuty.reader) && Objects.equals(book, bookDuty.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, book, days, penni);
    }
}
