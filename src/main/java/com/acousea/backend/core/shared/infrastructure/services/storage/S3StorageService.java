package com.acousea.backend.core.shared.infrastructure.services.storage;

import com.acousea.backend.core.shared.application.services.StorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class S3StorageService implements StorageService {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public S3StorageService(AmazonS3 s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(String path, File file) {
        s3Client.putObject(new PutObjectRequest(bucketName, path, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucketName, path).toString();
    }

    @Override
    public File downloadFile(String path) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, path);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        File file = new File("/tmp/" + path);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] read_buf = new byte[1024];
        int read_len;
        while ((read_len = inputStream.read(read_buf)) > 0) {
            outputStream.write(read_buf, 0, read_len);
        }
        inputStream.close();
        outputStream.close();
        return file;
    }

    @Override
    public boolean deleteFile(String path) {
        s3Client.deleteObject(bucketName, path);
        return true;
    }

    @Override
    public String getFileUrl(String path) {
        return s3Client.getUrl(bucketName, path).toString();
    }
}

