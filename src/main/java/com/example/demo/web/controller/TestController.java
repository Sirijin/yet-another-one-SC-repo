package com.example.demo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/admin")
    public String admin() {
        return "test admin";
    }

    @GetMapping("/user")
    public String user() {
        return "test user";
    }
}
