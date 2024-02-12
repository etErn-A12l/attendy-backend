package com.attendy.attendy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String root() {
        return "Server is up and running !";
    }

    @PostMapping("/post")
    public String post() {
        return "Post is also working !!";
    }
}
