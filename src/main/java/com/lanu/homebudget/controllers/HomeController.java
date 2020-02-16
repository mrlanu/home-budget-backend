package com.lanu.homebudget.controllers;

import com.lanu.homebudget.services.FirebaseStorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final FirebaseStorageService firebaseStorageService;

    public HomeController(FirebaseStorageService firebaseStorageService) {
        this.firebaseStorageService = firebaseStorageService;
    }

    @GetMapping("/")
    public String getHomePage(){
        return "Welcome to Home Budget by Lanu";
    }

    @GetMapping("/test")
    public void test(){
        try {
            firebaseStorageService.saveImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
