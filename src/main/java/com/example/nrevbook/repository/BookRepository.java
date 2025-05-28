package com.example.nrevbook.repository;

import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
}
