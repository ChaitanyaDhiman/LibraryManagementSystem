package com.example.librarymanagementsystem.repositories;

import com.example.librarymanagementsystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    public Optional<Book> findByTitle(String title);
    public Optional<Book> findByIsbn(String isbn);
}
