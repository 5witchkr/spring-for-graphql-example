package com.example.demo.adaptor.inbound;


import com.example.demo.application.port.inbound.BookInboundPort;
import com.example.demo.application.port.inbound.BookInput;
import com.example.demo.application.port.inbound.BookOutput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookGraphQLWebAdaptor {
    private final BookInboundPort bookInboundPort;

    public BookGraphQLWebAdaptor(BookInboundPort bookInboundPort) {
        this.bookInboundPort = bookInboundPort;
    }

    @QueryMapping
    public List<BookOutput> books() {
        return bookInboundPort.getAll();
    }
    @QueryMapping
    public BookOutput book(@Argument String title) {
        return bookInboundPort.getByTitle(title);
    }
    @MutationMapping
    public BookOutput createBook(@Argument BookInput bookInput){
        return bookInboundPort.create(bookInput);
    }
    @MutationMapping
    public BookOutput updateBook(@Argument BookInput bookInput){
        return bookInboundPort.update(bookInput);
    }
    @MutationMapping
    public Boolean deleteBook(@Argument String bookId){
        return bookInboundPort.deleteById(bookId);
    }
}
