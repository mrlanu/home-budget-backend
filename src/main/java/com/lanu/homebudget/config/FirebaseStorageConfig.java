package com.lanu.homebudget.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseStorageConfig {

    // client SDK library
    @Bean
    public Storage getStorage(){
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials
                    .fromStream(new FileInputStream("src/main/resources/images-service.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StorageOptions.newBuilder()
                .setCredentials(credentials).build().getService();
    }
}
