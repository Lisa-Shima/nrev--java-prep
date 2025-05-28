package com.example.nrevbook.service;

import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.BookRepository;
import com.example.nrevbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    @Override
    public Book createBook(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public List<Book> getBooksForUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookRepo.findByUser(user);
    }

    @Override
    public Optional<Book> findByIdAndUsername(Long bookId, String username) {
        return userRepo.findByUsername(username)
                .flatMap(u -> bookRepo.findById(bookId)
                        .filter(b -> b.getUser().getId().equals(u.getId())));
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepo.deleteById(bookId);
    }
}
