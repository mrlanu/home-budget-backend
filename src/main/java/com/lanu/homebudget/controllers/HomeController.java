package com.lanu.homebudget.controllers;

import com.lanu.homebudget.services.FirebaseStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

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

    @PostMapping(
            path = "/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(Principal principal,
                                       @RequestParam("file") MultipartFile file) {
        firebaseStorageService.uploadUserProfileImage(principal.getName(), file);
    }

    @GetMapping("/image/download")
    public String downloadUserProfileImage(Principal principal) {
        return firebaseStorageService.downloadUserProfileImage(principal.getName());
    }
}
