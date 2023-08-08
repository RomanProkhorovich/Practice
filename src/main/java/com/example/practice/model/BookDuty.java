package com.example.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Data
public class BookDuty {
    private Reader reader;
    private Book book;
    private long days;
    private float penni;

    @Value("${library.fine}")
    private  float fine = 0.03F;

    @Value("${library.daysBeforeLargerFine}")
    private static int daysBeforeLargerFine = 15;
    @Value("${library.largerFineFactor}")
    private static float largerFineFactor = 2.0F;

    public BookDuty(Reader reader, Book book, long days) {
        this.reader = reader;
        this.book = book;
        this.days = days;
        this.penni = getPenni(days);
    }

    private  float getPenni(long days){
        if (days>= daysBeforeLargerFine)
            return days*fine*largerFineFactor;

        return days*fine;
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
