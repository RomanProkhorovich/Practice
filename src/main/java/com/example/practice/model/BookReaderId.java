package com.example.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BookReaderId implements Serializable {
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "reader_id")
    private Long readerId;


    public BookReaderId(Long bookId, Long readerId) {
        this.bookId = bookId;
        this.readerId = readerId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookReaderId that = (BookReaderId) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(readerId, that.readerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, readerId);
    }
}
