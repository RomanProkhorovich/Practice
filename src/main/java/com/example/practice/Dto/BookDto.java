package com.example.practice.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;

import java.time.Year;

@Data
public class BookDto {
    private Long id;
    private String authorName;
    private Year releasedAt;

    private String title;
    private Boolean archived=false;
}
