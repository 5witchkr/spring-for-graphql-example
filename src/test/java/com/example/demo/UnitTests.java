package com.example.demo;

import com.example.demo.application.domain.Book;
import com.example.demo.application.port.inbound.BookInput;
import com.example.demo.application.port.inbound.BookOutput;
import com.example.demo.application.port.outbound.BookOutboundPort;
import com.example.demo.application.service.BookService;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

    @TestFactory
    Stream<DynamicTest> testCoverage() {
        bookOutboundPort = new TestBookOutboundPort();
        bookService = new BookService(bookOutboundPort);

        return Stream.of(
                DynamicTest.dynamicTest("getByTitle", () -> {
                    bookService.getByTitle("책");
                }),
                DynamicTest.dynamicTest("getByTitle", () -> {
                    assertThatThrownBy(()-> bookService.getByTitle("없는책")).isInstanceOf(EntityNotFoundException.class);
                }),
                DynamicTest.dynamicTest("update", () -> {
                    BookInput bookInput = new BookInput("id","title",0);
                    bookService.update(bookInput);
                }),
                DynamicTest.dynamicTest("update", () -> {
                    BookInput bookInput = new BookInput("not","title",0);
                    assertThatThrownBy(() -> bookService.update(bookInput)).isInstanceOf(EntityNotFoundException.class);
                }),
                DynamicTest.dynamicTest("delete", () -> {
                    bookService.deleteById("id");
                })
        );
    }

    private static class TestBookOutboundPort implements BookOutboundPort {
        @Override
        public Optional<Book> findByTitle(String title) {
            if (title.equals("책")){
                return Optional.of(new Book("id","책",0));
            }
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
            return bookDomain;
        }

        @Override
        public Optional<Book> findById(String id) {
            if (id.equals("id")){
                return Optional.of(new Book("id","title",0));
            }
            return Optional.empty();
        }

        @Override
        public void deleteById(String bookId) {

        }
    }
}
