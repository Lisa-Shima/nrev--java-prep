package com.example.nrevbook.service;

import com.example.nrevbook.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book createBook(Book book);
    Page<Book> getBooksForUser(String username, Pageable pageable);
    Optional<Book> findByIdAndUsername(Long bookId, String username);
    Book updateBook(Book book);
    void deleteBook(Long bookId);
}
