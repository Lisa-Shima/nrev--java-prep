package com.example.nrevbook.service;

import com.example.nrevbook.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    Book createBook(Book book);
    List<Book> getBooksForUser(String username);
    Optional<Book> findByIdAndUsername(Long bookId, String username);
    Book updateBook(Book book);
    void deleteBook(Long bookId);
}
