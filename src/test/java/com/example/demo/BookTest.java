package com.example.demo;

import com.example.demo.application.domain.Book;
import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    public void test(){
        Book book = new Book("id", "title", 1);
        int page = book.getPage();
        String id = book.getId();
        String title = book.getTitle();
        Book updatedbook = book.updateTitle(null).updateTitle("updatedTitle");
    }
}
