package com.example.practice.model;

import com.example.practice.exception.FutureBookException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"author_name", "released_at", "title"}))
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;


    @NonNull
    @Column(nullable = false,
            name = "author_name")
    private String authorName;

    @NonNull
    @Column(nullable = false,
            name = "released_at")
    private Year releasedAt;

    @NonNull
    @Column(nullable = false,
            name = "title")
    private String title;

    @Column(name = "archived")
    private Boolean archived=false;


    public Book(@NonNull String authorName, @NonNull Year releasedAt, @NonNull String title) {
        if (releasedAt.isAfter(Year.now()))
            throw new FutureBookException();
        this.authorName = authorName;
        this.releasedAt = releasedAt;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(authorName, book.authorName) && Objects.equals(releasedAt, book.releasedAt) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName, releasedAt, title);
    }
}
