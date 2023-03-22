package com.example.demo.application.port.inbound;

import java.util.List;

public interface BookInboundPort {
    List<BookOutput> getAll();

    BookOutput getByTitle(String title);

    BookOutput create(BookInput bookInput);

    BookOutput update(BookInput bookInput);

    Boolean deleteById(String bookId);

    List<BookOutput> recommendBook(int value);
}
