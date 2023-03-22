package com.example.demo;

import com.example.demo.application.domain.Book;
import com.example.demo.application.port.inbound.BookOutput;
import com.example.demo.application.port.outbound.BookOutboundPort;
import com.example.demo.application.service.BookService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class UnitTests {

    private BookService bookService;
    private BookOutboundPort bookOutboundPort;

    @TestFactory
    Stream<DynamicTest> test() {
        bookOutboundPort = new TestBookOutboundPort();
        bookService = new BookService(bookOutboundPort);
        List<Integer> testValues = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80);

        return testValues.stream().map(value ->
                DynamicTest.dynamicTest("인풋 값보다 페이지가 적은 책을 추천한다.", () -> {
                    List<BookOutput> result = bookService.recommendBook(value);

                    result.stream().peek(i -> {
                        assertThat(i).isInstanceOf(BookOutput.class);
                        assertThat(i.getPage()).isLessThan(value);
                        System.out.println(i.getTitle() + " is " +i.getPage());
                    }).count();
                }));
    }

    private static class TestBookOutboundPort implements BookOutboundPort {
        @Override
        public Optional<Book> findByTitle(String title) {
            return Optional.empty();
        }

        @Override
        public List<Book> findAll() {
            return IntStream.range(1,100)
                    .mapToObj(value -> new Book("ID"+value, "Title"+value, value))
                    .collect(Collectors.toList());
        }

        @Override
        public Book save(Book bookDomain) {
            return null;
        }

        @Override
        public Optional<Book> findById(String id) {
            return Optional.empty();
        }

        @Override
        public void deleteById(String bookId) {

        }
    }
}
