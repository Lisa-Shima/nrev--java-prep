package com.example.nrevbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminBookResponse {
    private Long   id;
    private String title;
    private String author;
    private String username;
}
