package com.example.practice.repository;

import com.example.practice.model.BookReaderId;
import com.example.practice.model.Log;
import com.example.practice.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, BookReaderId> {

    List<Log> findAllByIssueDateBefore(LocalDate deadline);
    List<Log> findAllByReader(Reader reader);

}
