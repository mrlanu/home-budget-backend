package com.lanu.homebudget.services.implementations;

import com.google.cloud.storage.*;
import com.lanu.homebudget.services.FirebaseStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import static org.apache.http.entity.ContentType.*;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    private final Storage storage;

    public FirebaseStorageServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void uploadUserProfileImage(String username, MultipartFile file) {

        isFileEmpty(file);
        isImage(file);

        BlobId blobId = BlobId.of("home-budget-lanu.appspot.com", username);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        try {
            Blob blob = storage.create(blobInfo, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String downloadUserProfileImage(String username) {
        BlobId blobId = BlobId.of("home-budget-lanu.appspot.com", username);
        return Base64.getEncoder().encodeToString(storage.get(blobId).getContent());
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }


}
