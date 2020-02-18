package com.lanu.homebudget.services;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseStorageService {
    void uploadUserProfileImage(String username, MultipartFile file);
    byte[] downloadUserProfileImage(String username);
}
