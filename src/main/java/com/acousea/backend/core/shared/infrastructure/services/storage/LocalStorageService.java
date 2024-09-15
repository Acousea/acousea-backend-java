package com.acousea.backend.core.shared.infrastructure.services;

import com.acousea.backend.core.shared.application.services.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalStorageService implements StorageService {

    private final String storageDirectory;

    public LocalStorageService(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    @Override
    public String uploadFile(String path, File file) throws IOException {
        Path destinationPath = Paths.get(storageDirectory, path);
        Files.copy(file.toPath(), destinationPath);
        return destinationPath.toUri().toString();
    }

    @Override
    public File downloadFile(String path) {
        return new File(Paths.get(storageDirectory, path).toString());
    }

    @Override
    public boolean deleteFile(String path) throws IOException {
        Path filePath = Paths.get(storageDirectory, path);
        return Files.deleteIfExists(filePath);
    }

    @Override
    public String getFileUrl(String path) {
        return Paths.get(storageDirectory, path).toUri().toString();
    }
}

