package com.example.demo.application.port.inbound;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookOutput {
    private String id;
    private String title;
    private int page;

    @Builder
    public BookOutput(String id, String title, int page){
        this.id = id;
        this.title = title;
        this.page = page;
    }
}
