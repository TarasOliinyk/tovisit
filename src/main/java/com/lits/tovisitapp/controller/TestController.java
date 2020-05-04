package com.lits.tovisitapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping
    public String get() {
        return "Hello World!";
    }
}
