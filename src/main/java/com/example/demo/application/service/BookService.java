package com.example.demo.application.service;


import com.example.demo.application.port.inbound.BookInput;
import com.example.demo.application.port.inbound.BookOutput;
import com.example.demo.application.domain.Book;
import com.example.demo.application.port.inbound.BookInboundPort;
import com.example.demo.application.port.outbound.BookOutboundPort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookService implements BookInboundPort {
    private final BookOutboundPort bookOutboundPort;

    public BookService(BookOutboundPort bookOutboundPort) {
        this.bookOutboundPort = bookOutboundPort;
    }
    @Override
    public BookOutput getByTitle(String title) {
        Book book = bookOutboundPort.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책"));
        return bookToBookOutput(book);
    }
    @Override
    public List<BookOutput> getAll() {
        return bookOutboundPort.findAll().stream()
                .map(this::bookToBookOutput)
                .collect(Collectors.toList());
    }
    @Override
    public BookOutput create(BookInput bookInput) {
        Book bookDomain = new Book(
                UUID.randomUUID().toString(),
                bookInput.getTitle(),
                bookInput.getPage());
        Book savedBook = bookOutboundPort.save(bookDomain);
        return bookToBookOutput(savedBook);
    }
    @Override
    public BookOutput update(BookInput bookInput) {
        Book bookDomain = bookOutboundPort.findById(bookInput.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책"))
                .updateTitle(bookInput.getTitle());
        Book savedBook = bookOutboundPort.save(bookDomain);
        return bookToBookOutput(savedBook);
    }
    @Override
    public Boolean deleteById(String bookId) {
        bookOutboundPort.deleteById(bookId);
        return true;
    }

    @Override
    public List<BookOutput> recommendBook(int value) {
        return bookOutboundPort.findAll().stream()
                .filter(i -> i.getPage() < value)
                .map(this::bookToBookOutput)
                .collect(Collectors.toList());
    }

    private BookOutput bookToBookOutput(Book book){
        return BookOutput.builder()
                .id(book.getId())
                .title(book.getTitle())
                .page(book.getPage())
                .build();
    }
}
