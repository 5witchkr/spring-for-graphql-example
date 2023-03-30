package com.example.demo.adaptor.inbound;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OauthWebAdaptor implements ErrorController {
    @GetMapping({"/", "/error"})
    public String reactPage() {
        return "oauth.html";
    }
}
