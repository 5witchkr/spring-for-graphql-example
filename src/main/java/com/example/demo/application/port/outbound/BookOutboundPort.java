package com.example.demo.application.port.outbound;

import com.example.demo.application.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookOutboundPort {
    Optional<Book> findByTitle(String title);
    List<Book> findAll();
    Book save(Book bookDomain);
    Optional<Book> findById(String id);
    void deleteById(String bookId);
}
