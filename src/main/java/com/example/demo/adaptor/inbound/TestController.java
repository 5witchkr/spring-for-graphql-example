package com.example.demo.adaptor.inbound;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        System.out.println("call");
        return "테스트 API 입니다.";
    }

    @GetMapping("/test123")
    public String test123(){
        System.out.println("call");
        return "테스트 API 입니다.";
    }
}
