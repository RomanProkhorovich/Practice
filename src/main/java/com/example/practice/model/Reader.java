package com.example.practice.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Reader {
    {
        isActive=true;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    private String firstname;

    @NonNull
    @Column(nullable = false)
    private String lastname;

    private String surname; //nullable потому что не у всех есть отчество

    @Column(name = "active")
    @Builder.Default
    private Boolean isActive=true;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role=Role.USER;


}
