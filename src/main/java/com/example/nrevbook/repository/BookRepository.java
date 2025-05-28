package com.example.nrevbook.repository;

import com.example.nrevbook.model.Book;
import com.example.nrevbook.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByUser(User user, Pageable pageable);
}
