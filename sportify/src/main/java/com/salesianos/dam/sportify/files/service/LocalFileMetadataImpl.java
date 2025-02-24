package com.salesianos.dam.sportify.files.service;

import com.salesianos.dam.sportify.files.model.AbstractFileMetadata;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}