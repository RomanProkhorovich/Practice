package com.example.practice.repository;

import com.example.practice.model.Log;
import com.example.practice.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByIssueDateBeforeAndReturnedDate(LocalDate issueDateMax,LocalDate endDate);
    List<Log> findAllByReader(Reader reader);
    List<Log> findAllByIssueDateBetween(LocalDate start, LocalDate end);

}
