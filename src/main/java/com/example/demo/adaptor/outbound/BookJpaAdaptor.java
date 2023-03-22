package com.example.demo.adaptor.outbound;

import com.example.demo.adaptor.outbound.entity.BookEntity;
import com.example.demo.application.domain.Book;
import com.example.demo.application.port.outbound.BookOutboundPort;
import com.example.demo.adaptor.outbound.repository.BookJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookJpaAdaptor implements BookOutboundPort {
    private final BookJpaRepository bookJpaRepository;
    public BookJpaAdaptor(BookJpaRepository bookJpaRepository) {
        this.bookJpaRepository = bookJpaRepository;
    }
    @Override
    public Optional<Book> findByTitle(String title) {
        return bookJpaRepository.findByTitle(title).map(this::bookToBookDomain);
    }
    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll().stream().map(this::bookToBookDomain).collect(Collectors.toList());
    }
    @Override
    public Book save(Book bookDomain) {
        BookEntity savedBook = bookJpaRepository.save(bookDomainToBook(bookDomain));
        return bookToBookDomain(savedBook);
    }
    @Override
    public Optional<Book> findById(String id) {
        return bookJpaRepository.findById(id).map(this::bookToBookDomain);
    }
    @Override
    public void deleteById(String bookId) {
        bookJpaRepository.deleteById(bookId);
    }

    private Book bookToBookDomain(BookEntity book){
        if (book == null) return null;
        return new Book(book.getId(), book.getTitle(), book.getPage());
    }

    private BookEntity bookDomainToBook(Book bookDomain){
        if (bookDomain == null) return null;
        return new BookEntity(bookDomain.getId(), bookDomain.getTitle(), bookDomain.getPage());
    }
}
