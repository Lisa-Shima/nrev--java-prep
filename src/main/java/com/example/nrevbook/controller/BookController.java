package com.example.nrevbook.controller;

import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.UserRepository;
import com.example.nrevbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserRepository userRepository;

    /** List all books for current user **/
    @GetMapping
    public ResponseEntity<List<Book>> listMyBooks(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(bookService.getBooksForUser(username));
    }

    /** Create a new book **/
    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book,
            Authentication auth) {

        // 1. Look up the User entity for the current username
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Associate the book with that user
        book.setUser(user);

        // 3. Persist
        Book saved = bookService.createBook(book);

        // 4. Return 201 with Location header
        return ResponseEntity
                .created(URI.create("/api/books/" + saved.getId()))
                .body(saved);
    }

    /** Get one book (if you own it) **/
    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(
            @PathVariable Long id,
            Authentication auth) {

        return bookService.findByIdAndUsername(id, auth.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    /** Update a book you own **/
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book updated,
            Authentication auth) {

        return bookService.findByIdAndUsername(id, auth.getName())
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setAuthor(updated.getAuthor());
                    Book saved = bookService.updateBook(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.status(404).build());
    }

    /** Delete a book you own **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id,
            Authentication auth) {

        return bookService.findByIdAndUsername(id, auth.getName())
                .map(b -> {
                    bookService.deleteBook(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.status(404).build());
    }
}
