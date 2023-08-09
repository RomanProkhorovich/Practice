package com.example.practice.Dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NonNull;

@Data
public class RegDto {
    @NonNull
    private final String email;

    @NonNull
    private final String password;

    @NonNull
    private final String firstname;

    @NonNull
    private final String lastname;

    private String surname; //nullable потому что не у всех есть отчество
}
