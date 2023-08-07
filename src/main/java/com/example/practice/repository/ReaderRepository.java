package com.example.practice.repository;

import com.example.practice.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader,Long> {

    Optional<Reader> findByEmail(String email);
    List<Reader> findAllByIsActive(Boolean active);
}
