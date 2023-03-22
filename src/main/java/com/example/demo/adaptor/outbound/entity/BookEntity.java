package com.example.demo.adaptor.outbound.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class BookEntity {
    @Id
    private String id;
    private String title;
    private Integer page;

    public BookEntity(){}
    public BookEntity(String id, String title, Integer page){
        this.id = id;
        this.title = title;
        this.page = page;
    }
}
