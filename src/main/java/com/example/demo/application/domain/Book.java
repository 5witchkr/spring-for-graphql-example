package com.example.demo.application.domain;

public final class Book {
    private final String id;
    private final String title;
    private final int page;
    public Book(String id, String title, int page){
        this.id = id;
        this.title = title;
        this.page = page;
    }
    public Book updateTitle(String title){
        if (title == null) return this;
        return new Book(this.id, title, this.page);
    }
    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public int getPage(){
        return this.page;
    }

}
