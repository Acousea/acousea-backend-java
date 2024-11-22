package com.acousea.backend.core.shared.application.services;

import java.io.File;

public interface StorageService {

    String uploadFile(String path, File file) throws Exception;

    File downloadFile(String path) throws Exception;

    boolean deleteFile(String path) throws Exception;

    String getFileUrl(String path);
}
