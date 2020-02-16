package com.lanu.homebudget.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseStorageConfig {

    @Bean
    public Bucket getBucket(){
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("/home/lanu/development/MyProjects/Java-apps/home-budget/key.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("home-budget-lanu.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return StorageClient.getInstance().bucket();
    }
}
