package com.example.practice.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Response {
    private boolean authenticated;
    private String username;
    private String role;
}
