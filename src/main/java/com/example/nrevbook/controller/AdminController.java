package com.example.nrevbook.controller;

import com.example.nrevbook.dto.AdminBookResponse;
import com.example.nrevbook.dto.AuditLogResponse;
import com.example.nrevbook.dto.BookResponse;
import com.example.nrevbook.dto.PagedResponse;
import com.example.nrevbook.model.AuditLog;
import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.AuditLogRepository;
import com.example.nrevbook.repository.BookRepository;
import com.example.nrevbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    private final AuditLogRepository auditLogRepo;

    // List all users (you probably already have this)
    @Operation(summary = "Getting all users")
    @GetMapping("/users")
    public List<User> listAllUsers() {
        return userRepo.findAll();
    }

    // NEW: List all books, mapped to DTO
    @Operation(summary = "Listing all books")
    @GetMapping("/books")
    public ResponseEntity<PagedResponse<AdminBookResponse>> listAllBooks(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="20") int size) {

        Pageable pg = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLog> logs = auditLogRepo.findAll(pg);

        // or if you want books:
        Page<Book> books = bookRepo.findAll(pg);

        List<AdminBookResponse> dtos = books.getContent().stream()
                .map(b -> new AdminBookResponse(
                        b.getId(), b.getTitle(), b.getAuthor(), b.getUser().getUsername()
                ))
                .toList();

        PagedResponse<AdminBookResponse> resp = new PagedResponse<>(
                dtos,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast()
        );
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/logs")
    public List<AuditLogResponse> listAuditLogs() {
        return auditLogRepo.findAll().stream()
                .map(l -> new AuditLogResponse(
                        l.getId(),
                        l.getEntityType(),
                        l.getEntityId(),
                        l.getAction(),
                        l.getUsername(),
                        l.getTimestamp()
                ))
                .collect(Collectors.toList());
    }
}
