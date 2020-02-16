package com.lanu.homebudget.services.implementations;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import com.lanu.homebudget.services.FirebaseStorageService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    private final Bucket bucket;

    public FirebaseStorageServiceImpl(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public String saveImage() {
        return bucket.create("Test", "Test new".getBytes()).getName();
    }

    @Override
    public String saveImageClient() {
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials
                    .fromStream(new FileInputStream("/home/lanu/development/MyProjects/Java-apps/home-budget/images-service-key.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials).build().getService();

        BlobId blobId = BlobId.of("home-budget-lanu.appspot.com", "blob_name");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }

        return null;
    }
}
