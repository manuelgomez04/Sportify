package com.salesianos.dam.sportify.liga.controller;

import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.service.LigaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/liga")
@RequiredArgsConstructor
public class LigaController {

    private final LigaService ligaService;


    @PostMapping
    public ResponseEntity<Liga> createLiga(@RequestBody CreateLigaRequest createLigaRequest) {
        return ResponseEntity.ok(ligaService.createLiga(createLigaRequest));
    }
}
