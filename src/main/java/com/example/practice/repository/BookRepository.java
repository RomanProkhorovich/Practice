package com.example.practice.repository;

import com.example.practice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByAuthorNameAndReleasedAtAndTitle(String author, Year releaseAt, String title);
    List<Book> findAllByArchived(Boolean archived);
}
