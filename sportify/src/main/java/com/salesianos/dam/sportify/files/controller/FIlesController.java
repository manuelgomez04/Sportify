package com.salesianos.dam.sportify.files.controller;

import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.files.utils.MimeTypeDetector;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FIlesController {

    private final StorageService storageService;
    private final MimeTypeDetector mimeTypeDetector;


    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        Resource resource =  storageService.loadAsResource(id);

        String mimeType = mimeTypeDetector.getMimeType(resource);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", mimeType)
                .body(resource);
    }

}

