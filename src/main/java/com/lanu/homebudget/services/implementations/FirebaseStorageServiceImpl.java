package com.lanu.homebudget.services.implementations;

import com.google.cloud.storage.Bucket;
import com.lanu.homebudget.services.FirebaseStorageService;
import org.springframework.stereotype.Service;

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
}
