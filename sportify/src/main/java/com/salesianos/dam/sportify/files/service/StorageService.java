package com.salesianos.dam.sportify.files.service;

import com.salesianos.dam.sportify.files.model.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    FileMetadata store(MultipartFile file);

    Resource loadAsResource(String id);

    void deleteFile(String filename);


}
