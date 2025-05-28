package com.example.nrevbook.controller;

import com.example.nrevbook.dto.BookRequest;
import com.example.nrevbook.dto.BookResponse;
import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.UserRepository;
import com.example.nrevbook.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Books", description = "Operations on user-owned books")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserRepository userRepository;

    @Operation(summary = "List all books for the logged-in user")
    @GetMapping
    public ResponseEntity<List<BookResponse>> listMyBooks(Authentication auth) {
        List<BookResponse> dtos = bookService
                .getBooksForUser(auth.getName())
                .stream()
                .map(b -> new BookResponse(b.getId(), b.getTitle(), b.getAuthor()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Creates books for the logged-in user")
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest req,
            Authentication auth) {

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setUser(user);

        Book saved = bookService.createBook(book);
        BookResponse dto = new BookResponse(saved.getId(), saved.getTitle(), saved.getAuthor());

        return ResponseEntity
                .created(URI.create("/api/books/" + saved.getId()))
                .body(dto);
    }

    @Operation(summary = "Gets a book with id for the logged-in user")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getOne(
            @PathVariable Long id,
            Authentication auth) {

        return bookService
                .findByIdAndUsername(id, auth.getName())
                .map(b -> new BookResponse(b.getId(), b.getTitle(), b.getAuthor()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Edits a book with id for the logged-in user")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest req,
            Authentication auth) {

        return bookService.findByIdAndUsername(id, auth.getName())
                .map(existing -> {
                    existing.setTitle(req.getTitle());
                    existing.setAuthor(req.getAuthor());
                    Book updated = bookService.updateBook(existing);
                    return new BookResponse(updated.getId(), updated.getTitle(), updated.getAuthor());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletes a book with id for the logged-in user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(
            @PathVariable Long id,
            Authentication auth) {

        return bookService.findByIdAndUsername(id, auth.getName())
                .map(b -> {
                    bookService.deleteBook(id);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
