package com.lanu.homebudget.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String getHomePage(){
        return "Welcome to Home Budget by Lanu";
    }
}
