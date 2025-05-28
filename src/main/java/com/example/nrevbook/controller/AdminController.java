package com.example.nrevbook.controller;

import com.example.nrevbook.dto.AdminBookResponse;
import com.example.nrevbook.dto.BookResponse;
import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.BookRepository;
import com.example.nrevbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Admin", description = "Admin accessed routes")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepo;
    private final BookRepository bookRepo;

    // List all users (you probably already have this)
    @Operation(summary = "Getting all users")
    @GetMapping("/users")
    public List<User> listAllUsers() {
        return userRepo.findAll();
    }

    // NEW: List all books, mapped to DTO
    @Operation(summary = "Listing all books")
    @GetMapping("/books")
    public List<AdminBookResponse> listAllBooks() {
        return bookRepo.findAll().stream()
                .map(b -> new AdminBookResponse(
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor(),
                        // unwrap the user proxy to just get the username
                        b.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }
}
