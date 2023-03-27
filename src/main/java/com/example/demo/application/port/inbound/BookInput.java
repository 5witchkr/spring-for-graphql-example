package com.example.demo.application.port.inbound;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInput {
    private String id;
    private String title;
    private int page;

    public BookInput (String id, String title, int page){
        this.id = id;
        this.title = title;
        this.page = page;
    }
}
